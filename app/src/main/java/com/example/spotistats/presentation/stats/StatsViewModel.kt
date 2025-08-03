package com.example.spotistats.presentation.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotistats.domain.model.Album
import com.example.spotistats.domain.model.TopAlbum
import com.example.spotistats.domain.model.UserTopArtists
import com.example.spotistats.domain.model.UserTopTracks
import com.example.spotistats.domain.useCases.CalculateTopAlbumsUseCase
import com.example.spotistats.domain.useCases.TopContentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class StatsViewModel @Inject constructor(
    private val topContentUseCase: TopContentUseCase,
    private val calculateTopAlbumsUseCase: CalculateTopAlbumsUseCase,
):ViewModel() {
    private val _uiState = MutableStateFlow(StatsUiState())
    val uiState:StateFlow<StatsUiState> = _uiState

    fun updateContentType(contentType: ContentType){
        _uiState.value = _uiState.value.copy(selectedContentType = contentType)
        loadStats()
    }

    fun updateTimeRange(timeRange: TimeRange){
       _uiState.value = _uiState.value.copy(selectedTimeRange = timeRange)
        loadStats()
    }

    fun loadStats() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val currentType = _uiState.value.selectedContentType
                val currentRange = _uiState.value.selectedTimeRange

                _uiState.value = _uiState.value.copy(
                    topTracks = null,
                    topArtists = null,
                    topAlbums = emptyList()
                )

                when (currentType) {

                    ContentType.TRACKS -> {
                        val tracks = when (currentRange) {
                            TimeRange.SHORT -> topContentUseCase.getUserTopTracksShort()
                            TimeRange.MEDIUM -> topContentUseCase.getUserTopTracksMedium()
                            TimeRange.LONG -> topContentUseCase.getUserTopTracksLong()
                        }
                        _uiState.value = _uiState.value.copy(topTracks = tracks)

                    }

                    ContentType.ARTISTS -> {
                        val artists = when (currentRange) {
                            TimeRange.SHORT -> topContentUseCase.getUserTopArtistsShort()
                            TimeRange.MEDIUM -> topContentUseCase.getUserTopArtistsMedium()
                            TimeRange.LONG -> topContentUseCase.getUserTopArtistsLong()
                        }
                        _uiState.value = _uiState.value.copy(topArtists = artists)
                    }

                    ContentType.ALBUMS -> {
                        val tracks = when (currentRange) {
                            TimeRange.SHORT -> topContentUseCase.getUserTopTracksShort()
                            TimeRange.MEDIUM -> topContentUseCase.getUserTopTracksMedium()
                            TimeRange.LONG -> topContentUseCase.getUserTopTracksLong()
                        }
                        val albums = calculateTopAlbumsUseCase(tracks)
                        _uiState.value = _uiState.value.copy(topAlbums = albums)
                    }
                }
            }catch (e:Exception){
                e.printStackTrace()
            }finally {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }
}
