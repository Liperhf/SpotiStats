package com.example.spotistats.presentation.screen.authorization

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotistats.domain.model.Album
import com.example.spotistats.domain.model.TopAlbum
import com.example.spotistats.domain.model.UserTopArtists
import com.example.spotistats.domain.model.UserTopTracks
import com.example.spotistats.domain.useCases.SpotifyAuthUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class StatsViewModel @Inject constructor(
    private val spotifyAuthUseCase: SpotifyAuthUseCase,
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

    fun updateContentType(contentType:ContentType){
        _selectedContentType.value = contentType
        loadStats()
    }

    fun updateTimeRange(timeRange: TimeRange){
        _selectedTimeRange.value = timeRange
        loadStats()
    }

    fun loadStats() {
        viewModelScope.launch {

            _isLoading.value = false
            try {
                val currentType = _selectedContentType.value
                val currentRange = _selectedTimeRange.value

                _topTracks.value = null
                _topArtists.value = null
                _topAlbums.value = emptyList()

                when (currentType) {

                    ContentType.TRACKS -> {
                        _topTracks.value = when (currentRange) {
                            TimeRange.SHORT -> spotifyAuthUseCase.getUserTopTracksShort()
                            TimeRange.MEDIUM -> spotifyAuthUseCase.getUserTopTracksMedium()
                            TimeRange.LONG -> spotifyAuthUseCase.getUserTopTracksLong()
                        }
                    }

                    ContentType.ARTISTS -> {
                        _topArtists.value = when (currentRange) {
                            TimeRange.SHORT -> spotifyAuthUseCase.getUserTopArtistsShort()
                            TimeRange.MEDIUM -> spotifyAuthUseCase.getUserTopArtistsMedium()
                            TimeRange.LONG -> spotifyAuthUseCase.getUserTopArtistsLong()
                        }
                    }

                    ContentType.ALBUMS -> {
                        viewModelScope.launch {
                            val tracks = when (currentRange) {
                                TimeRange.SHORT -> spotifyAuthUseCase.getUserTopTracksShort()
                                TimeRange.MEDIUM -> spotifyAuthUseCase.getUserTopTracksMedium()
                                TimeRange.LONG -> spotifyAuthUseCase.getUserTopTracksLong()
                            }
                            calculatedUserTopAlbums(tracks)
                        }
                    }
                }
            }catch (e:Exception){
                e.printStackTrace()
            }finally {
                _isLoading.value = true
            }
            }


        }


    private fun calculatedUserTopAlbums(userTopTracks:UserTopTracks){
        viewModelScope.launch {
            if(userTopTracks.items.isEmpty()){
                _topAlbums.value = emptyList()
                return@launch
            }

            val albumsCount = mutableMapOf<String, Pair<Album,Int>>()
            userTopTracks.items.forEach() { track ->
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
                _topAlbums.value = sortedTopAlbums.take(10)
            }

        }
    }

    }
