package com.example.spotistats.presentation.main

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotistats.R
import com.example.spotistats.domain.model.Album
import com.example.spotistats.domain.model.CurrentlyPlaying
import com.example.spotistats.domain.model.RecentlyPlayed
import com.example.spotistats.domain.model.TopAlbum
import com.example.spotistats.domain.model.UserProfile
import com.example.spotistats.domain.model.UserTopArtists
import com.example.spotistats.domain.model.UserTopTracks
import com.example.spotistats.domain.useCases.AuthUseCase
import com.example.spotistats.domain.useCases.CalculateTopAlbumsUseCase
import com.example.spotistats.domain.useCases.PlaybackUseCase
import com.example.spotistats.domain.useCases.TopContentUseCase
import com.example.spotistats.domain.useCases.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val playbackUseCase: PlaybackUseCase,
    private val userUseCase: UserUseCase,
    private val topContentUseCase: TopContentUseCase,
    private val calculateTopAlbumsUseCase: CalculateTopAlbumsUseCase
): ViewModel() {
    private val _recentlyPlayed = MutableStateFlow<RecentlyPlayed?>(null)
    val recentlyPlayed: StateFlow<RecentlyPlayed?> = _recentlyPlayed
    private val _userProfile = MutableStateFlow<UserProfile?>(null)
    val userProfile: StateFlow<UserProfile?> = _userProfile
    private val _currentlyPlaying = MutableStateFlow<CurrentlyPlaying?>(null)
    val currentlyPlaying: StateFlow<CurrentlyPlaying?> = _currentlyPlaying
    private val _progressMs = MutableStateFlow(0)
    val progressMs = _progressMs.asStateFlow()
    var progressJob: Job? = null
    private val _userTopArtists = MutableStateFlow<UserTopArtists?>(null)
    val userTopArtists: StateFlow<UserTopArtists?> = _userTopArtists
    private val _userTopTracks = MutableStateFlow<UserTopTracks?>(null)
    val userTopTracks: StateFlow<UserTopTracks?> = _userTopTracks
    private val _userTopAlbums = MutableStateFlow<List<TopAlbum?>>(emptyList())
    val userTopAlbums: StateFlow<List<TopAlbum?>> = _userTopAlbums



    suspend fun getRecentlyPlayed() {
        try {
            _recentlyPlayed.value = playbackUseCase.getRecentlyPlayed()
        } catch (e: Exception) {
            Log.e("getRecentlyPlayed", "No network: ${e.message}")
        }
    }

    suspend fun getUserProfile(){
        try {
            _userProfile.value = userUseCase.getUserProfile()
            println("Profile loaded: ${_userProfile.value?.imagesUrl}")
        } catch (e: Exception) {
            println("Failed to load profile: ${e.message}")
        }
    }

    suspend fun getCurrentlyPlaying(){
        try {
            _currentlyPlaying.value = playbackUseCase.getCurrentlyPlaying()
            val data = _currentlyPlaying.value
            val durationMs = data?.item?.duration_ms ?: 0
            _progressMs.value = data?.progress_ms ?: 0
            progressJob?.cancel()
            if(durationMs > 0 ){
                progressJob = startProgress(durationMs)
            }
        }catch (e:Exception){
            _currentlyPlaying.value = null
            Log.e("NowPlaying", "Failed to load : ${e.message}")
        }
    }

    suspend fun getUserTopArtists() {
        try {
            val result = topContentUseCase.getUserTopArtistsShort()
            _userTopArtists.value = result
        } catch (e: Exception) {
            println("Failed to load top artists: ${e.message}")
        }
    }

    suspend fun getUserTopTracks() {
        try {
            val result = topContentUseCase.getUserTopTracksShort()
            _userTopTracks.value = result
            calculatedUserTopAlbums()
        }catch (e:Exception){
            println("Failed to load top artists: ${e.message}")
        }
    }

    private fun startProgress(durationMs:Int) : Job {
        return viewModelScope.launch {
            while(_progressMs.value < durationMs && _currentlyPlaying.value?.is_playing == true){
                delay(100)
                _progressMs.value += 100
            }
        }
    }

    private fun calculatedUserTopAlbums() {
        val topTracks = _userTopTracks.value ?: return
        val topAlbums = calculateTopAlbumsUseCase(topTracks)
        _userTopAlbums.value = topAlbums
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



}