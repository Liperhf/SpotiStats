package com.example.spotistats.presentation.main.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.spotistats.domain.model.CurrentlyPlaying
import com.example.spotistats.domain.model.TopAlbum
import com.example.spotistats.domain.model.Track
import com.example.spotistats.domain.model.UserTopArtists
import com.example.spotistats.domain.model.UserTopTracks
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.Job

@Composable
fun MainContent(isRefreshing: MutableState<Boolean>,
                refreshRecentlyPlayed: () -> Job,
                paddingValues: PaddingValues,
                currentlyPlaying: State<CurrentlyPlaying?>,
                currentlyImageUrl:String?,
                currentlyTrackName: String?,
                currentlyArtistName: String?,
                currentlyDurationMs:Int,
                progressMs:State<Int>,
                lastPlayedTrack:String?,
                lastPlayedImageUrl: String?,
                lastPlayedAlbum: String?,
                lastPlayedName:String?,
                lastPlayedArtist:String?,
                recentlyPlayedTracks:List<Track>?,
                onRecentlyShowClick:()->Unit,
                userTopArtists:State<UserTopArtists?>,
                userTopTracks:State<UserTopTracks?>,
                userTopAlbums:State<List<TopAlbum?>>
                ){
    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing = isRefreshing.value),
        onRefresh = { refreshRecentlyPlayed() },
    ) {
        LazyColumn(modifier = Modifier.padding(paddingValues)) {
            item {
                when {
                    currentlyPlaying.value?.is_playing == true -> {
                        if (currentlyImageUrl != null && currentlyTrackName != null && currentlyArtistName != null) {
                            NowPlayingBox(
                                currentlyImageUrl,
                                currentlyTrackName,
                                currentlyArtistName,
                                currentlyDurationMs,
                                progressMs.value
                            )
                        }
                    }

                    lastPlayedTrack != null -> {
                        if (lastPlayedImageUrl != null && lastPlayedName != null && lastPlayedArtist != null) {
                            LastPlayedBox(
                                lastPlayedImageUrl,
                                lastPlayedName,
                                lastPlayedArtist
                            )
                        }
                    }
                }
            }
            item {
                if (recentlyPlayedTracks != null) {
                    RecentlyPlayedBox(
                        recentlyPlayedTracks = recentlyPlayedTracks,
                        onShowAllClick = {onRecentlyShowClick()}
                    )
                }
            }
            item {
                userTopArtists.value?.let { UserTopArtistsBox(it) }
            }
            item {
                userTopTracks.value?.let { UserTopTracksBox(it) }
            }
            item {
                UserTopAlbumsBox(userTopAlbums.value)
            }
        }

    }
}