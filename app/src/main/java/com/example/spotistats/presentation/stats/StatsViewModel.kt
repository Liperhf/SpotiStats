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
    enum class ContentType{TRACKS,ARTISTS,ALBUMS}
    enum class TimeRange{SHORT,MEDIUM,LONG}
    private val _selectedContentType = MutableStateFlow(ContentType.TRACKS)
    val selectedContentType:StateFlow<ContentType> = _selectedContentType
    private val _selectedTimeRange = MutableStateFlow(TimeRange.SHORT)
    val selectedTimeRange:StateFlow<TimeRange> = _selectedTimeRange
    val _topTracks = MutableStateFlow<UserTopTracks?>(null)
    val topTracks:StateFlow<UserTopTracks?> = _topTracks
    private val _topArtists = MutableStateFlow<UserTopArtists?>(null)
    val topArtists:StateFlow<UserTopArtists?> = _topArtists
    private val _topAlbums = MutableStateFlow<List<TopAlbum?>>(emptyList())
    val topAlbums:StateFlow<List<TopAlbum?>> = _topAlbums
    private val _isLoading = MutableStateFlow(false)
    val isLoading:StateFlow<Boolean?> = _isLoading

    fun updateContentType(contentType: ContentType){
        _selectedContentType.value = contentType
        loadStats()
    }

    fun updateTimeRange(timeRange: TimeRange){
        _selectedTimeRange.value = timeRange
        loadStats()
    }

    fun loadStats() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val currentType = _selectedContentType.value
                val currentRange = _selectedTimeRange.value

                _topTracks.value = null
                _topArtists.value = null
                _topAlbums.value = emptyList()

                when (currentType) {

                    ContentType.TRACKS -> {
                        _topTracks.value = when (currentRange) {
                            TimeRange.SHORT -> topContentUseCase.getUserTopTracksShort()
                            TimeRange.MEDIUM -> topContentUseCase.getUserTopTracksMedium()
                            TimeRange.LONG -> topContentUseCase.getUserTopTracksLong()
                        }
                    }

                    ContentType.ARTISTS -> {
                        _topArtists.value = when (currentRange) {
                            TimeRange.SHORT -> topContentUseCase.getUserTopArtistsShort()
                            TimeRange.MEDIUM -> topContentUseCase.getUserTopArtistsMedium()
                            TimeRange.LONG -> topContentUseCase.getUserTopArtistsLong()
                        }
                    }

                    ContentType.ALBUMS -> {
                        val tracks = when (currentRange) {
                            TimeRange.SHORT -> topContentUseCase.getUserTopTracksShort()
                            TimeRange.MEDIUM -> topContentUseCase.getUserTopTracksMedium()
                            TimeRange.LONG -> topContentUseCase.getUserTopTracksLong()
                        }
                        calculatedUserTopAlbums(tracks)
                    }
                }
            }catch (e:Exception){
                e.printStackTrace()
            }finally {
                _isLoading.value = false
            }
        }
    }


    private fun calculatedUserTopAlbums(userTopTracks:UserTopTracks) {
        val topAlbums = calculateTopAlbumsUseCase(userTopTracks)
        _topAlbums.value = topAlbums
    }
}
