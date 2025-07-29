package com.example.spotistats.presentation.screen.authorization

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotistats.R
import com.example.spotistats.data.dto.RecentlyPlayedDto
import com.example.spotistats.domain.model.Album
import com.example.spotistats.domain.model.CurrentlyPlaying
import com.example.spotistats.domain.model.RecentlyPlayed
import com.example.spotistats.domain.model.TopAlbum
import com.example.spotistats.domain.model.UserProfile
import com.example.spotistats.domain.model.UserTopArtists
import com.example.spotistats.domain.model.UserTopArtistsItem
import com.example.spotistats.domain.model.UserTopTracks
import com.example.spotistats.domain.useCases.SpotifyAuthUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.launch
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val spotifyAuthUseCase:SpotifyAuthUseCase,
) : ViewModel() {
    private val _authIntent = MutableStateFlow<Intent?>(null)
    val authIntent: StateFlow<Intent?> = _authIntent
    private val _recentlyPlayed = MutableStateFlow<RecentlyPlayed?>(null)
    val recentlyPlayed:StateFlow<RecentlyPlayed?> = _recentlyPlayed
    private val _isAuthenticated = MutableStateFlow(false)
    val isAuthenticated: StateFlow<Boolean> = _isAuthenticated
    private val _userTopArtists = MutableStateFlow<UserTopArtists?>(null)
    val userTopArtists:StateFlow<UserTopArtists?> = _userTopArtists
    private val _userTopTracks = MutableStateFlow<UserTopTracks?>(null)
    val userTopTracks:StateFlow<UserTopTracks?> = _userTopTracks
    private val _userTopAlbums = MutableStateFlow<List<TopAlbum?>>(emptyList())
    val userTopAlbums: StateFlow<List<TopAlbum?>> = _userTopAlbums
    private val _userProfile = MutableStateFlow<UserProfile?>(null)
    val userProfile:StateFlow<UserProfile?> = _userProfile
    private val _currentlyPlaying = MutableStateFlow<CurrentlyPlaying?>(null)
    val currentlyPlaying:StateFlow<CurrentlyPlaying?> = _currentlyPlaying
    private val _progressMs = MutableStateFlow(0)
    val progressMs = _progressMs.asStateFlow()
    var progressJob: Job? = null
    private val _callbackHandled = MutableStateFlow(false)
    val callbackHandled: StateFlow<Boolean> = _callbackHandled
    fun markCallbackHandled() { _callbackHandled.value = true }


    fun onLoginClicked(){
        _authIntent.value = spotifyAuthUseCase.createAuthIntent()
    }

    suspend fun getRecentlyPlayed() {
        try {
            _recentlyPlayed.value = spotifyAuthUseCase.getRecentlyPlayed()
        } catch (e: Exception) {
            Log.e("getRecentlyPlayed", "No network: ${e.message}")
        }
    }

    fun checkAuthStatus(){
        viewModelScope.launch {
            try{
                val isSuccess = spotifyAuthUseCase.isUserAuthorized()
                Log.d("AuthViewModel", "checkAuthStatus: isUserAuthorized = $isSuccess")
                _isAuthenticated.value = isSuccess
            }
            catch (e:Exception){
                Log.e("AuthViewModel", "checkAuthStatus error", e)
                _isAuthenticated.value = false
            }
        }
    }

    fun logout(){
        viewModelScope.launch {
            try{
                spotifyAuthUseCase.clearTokens()
                _isAuthenticated.value = false
            }
            catch (e:Exception){
                e.printStackTrace()
            }
        }
    }

    fun processAuthorizationCode(code:String){
        Log.d("AuthViewModel", "processAuthorizationCode: code = $code")
        viewModelScope.launch {
            try{
                val authDto = spotifyAuthUseCase.exchangeCodeForToken(code)
                Log.d("AuthViewModel", "exchangeCodeForToken success: $authDto")
                spotifyAuthUseCase.saveTokens(authDto)
                Log.d("AuthViewModel", "Tokens saved")
                checkAuthStatus()
            }
            catch (e:Exception){
                Log.e("AuthViewModel", "processAuthorizationCode error", e)
            }
        }
    }

    fun processSpotifyCallback(intent: Intent) {
        val data: Uri? = intent.data
        Log.d("AuthViewModel", "processSpotifyCallback: data = $data")
        if (data?.scheme == "spotistats" && data.host == "callback") {
            val code = data.getQueryParameter("code")
            val error = data.getQueryParameter("error")
            Log.d("AuthViewModel", "processSpotifyCallback: code = $code, error = $error")
            if (code != null) {
                processAuthorizationCode(code)
            } else if (error != null) {
                Log.e("AuthViewModel", "Authorization error: $error")
            }
        }
    }

    fun refreshToken(){
        viewModelScope.launch {
            try {
                val success = spotifyAuthUseCase.refreshTokenIfNeeded()
                if (!success) {
                    _isAuthenticated.value = false
                }
            }catch (e:Exception){
                e.printStackTrace()
                _isAuthenticated.value = false
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun getGreeting():Int{
        val hour = LocalTime.now().hour
        val resId = when(hour){
            in 6..11 -> R.string.good_morning
            in 12..17 -> R.string.good_day
            in 18..24 -> R.string.good_evening
            else -> R.string.good_night
        }
        return resId
    }


    suspend fun getUserProfile(){
        try {
            _userProfile.value = spotifyAuthUseCase.getUserProfile()
            println("Profile loaded: ${_userProfile.value?.imagesUrl}")
        } catch (e: Exception) {
            println("Failed to load profile: ${e.message}")
        }
    }
    suspend fun getCurrentlyPlaying(){
        try {
            _currentlyPlaying.value = spotifyAuthUseCase.getCurrentlyPlaying()
            val data = _currentlyPlaying.value
            val durationMs = data?.item?.duration_ms ?: 0
            _progressMs.value = data?.progress_ms ?: 0
            progressJob?.cancel()
            if(durationMs > 0 ){
                progressJob = startProgress(durationMs)
            }
        }catch (e:Exception){
            _currentlyPlaying.value = null
            Log.e("NowPlaying","Failed to load : ${e.message}")
        }
    }

    suspend fun getUserTopArtists() {
        try {
            val result = spotifyAuthUseCase.getUserTopArtistsShort()
            _userTopArtists.value = result
        } catch (e: Exception) {
            println("Failed to load top artists: ${e.message}")
        }
    }
    suspend fun getUserTopTracks() {
        try {
            val result = spotifyAuthUseCase.getUserTopTracksShort()
            _userTopTracks.value = result
            calculatedUserTopAlbums()
        }catch (e:Exception){
            println("Failed to load top artists: ${e.message}")
        }
    }

    fun calculatedUserTopAlbums(){
        if(userTopTracks == null){
            _userTopAlbums.value = emptyList()
            return
        }
        val albumsCount = mutableMapOf<String, Pair<Album,Int>>()
        userTopTracks.value?.items?.forEach() { track ->
            val album = track.album
            albumsCount.compute(album.id){_,pair ->
                if(pair == null){
                    Pair(album,1)
                }else{
                    Pair(album,pair.second + 1)
                }
            }
            val sortedTopAlbums = albumsCount.values
                .map {pair -> TopAlbum(pair.first,pair.second)  }
                .sortedByDescending {topAlbum: TopAlbum -> topAlbum.trackCount  }
            _userTopAlbums.value = sortedTopAlbums.take(10)
        }
    }

    private fun startProgress(durationMs:Int) : Job{
        return viewModelScope.launch {
            while(_progressMs.value < durationMs && _currentlyPlaying.value?.is_playing == true){
                delay(100)
                _progressMs.value += 100
            }
        }
    }
}