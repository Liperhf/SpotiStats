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
    private val _uiState = MutableStateFlow(MainUiState())
    val uiState = _uiState.asStateFlow()
    private var progressJob: Job? = null




    fun loadStats(){
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                getRecentlyPlayed()
                getUserProfile()
                getUserTopArtists()
                getUserTopTracks()
                getCurrentlyPlaying()
            }catch (e:Exception){
                e.printStackTrace()
            }
            _uiState.value = _uiState.value.copy(isLoading = false)
        }
    }


    suspend fun getRecentlyPlayed() {
        try {
            _uiState.value = _uiState.value.copy(recentlyPlayed = playbackUseCase.getRecentlyPlayed())
        } catch (e: Exception) {
            Log.e("getRecentlyPlayed", "No network: ${e.message}")
        }
    }

    suspend fun getUserProfile(){
        try {
            _uiState.value = _uiState.value.copy(userProfile = userUseCase.getUserProfile())
            println("Profile loaded: ${_uiState.value.userProfile?.imagesUrl}")
        } catch (e: Exception) {
            println("Failed to load profile: ${e.message}")
        }
    }

    suspend fun getCurrentlyPlaying(){
        try {
            _uiState.value = _uiState.value.copy(currentlyPlaying = playbackUseCase.getCurrentlyPlaying())
            val data = _uiState.value.currentlyPlaying
            val durationMs = data?.item?.duration_ms ?: 0
            _uiState.value = _uiState.value.copy(progressMs = data?.progress_ms ?: 0)
            progressJob?.cancel()
            if(durationMs > 0 ){
                progressJob = startProgress(durationMs)
            }
        }catch (e:Exception){
            _uiState.value = _uiState.value.copy(currentlyPlaying = null)
            Log.e("NowPlaying", "Failed to load : ${e.message}")
        }
    }

    suspend fun getUserTopArtists() {
        try {
            val result = topContentUseCase.getUserTopArtistsShort()
            _uiState.value = _uiState.value.copy(topArtists = result)
        } catch (e: Exception) {
            println("Failed to load top artists: ${e.message}")
        }
    }

    suspend fun getUserTopTracks() {
        try {
            val result = topContentUseCase.getUserTopTracksShort()
            _uiState.value = _uiState.value.copy(topTracks = result)
            calculatedUserTopAlbums()
        }catch (e:Exception){
            println("Failed to load top artists: ${e.message}")
        }
    }

    private fun startProgress(durationMs:Int) : Job {
        return viewModelScope.launch {
            while(_uiState.value.progressMs < durationMs && _uiState.value.currentlyPlaying?.is_playing == true){
                delay(100)
                _uiState.value = _uiState.value.copy(progressMs = _uiState.value.progressMs + 100)
            }
        }
    }

    private fun calculatedUserTopAlbums() {
        val topTracks = _uiState.value.topTracks ?: return
        val topAlbums = calculateTopAlbumsUseCase(topTracks)
        _uiState.value = _uiState.value.copy(topAlbums = topAlbums)
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