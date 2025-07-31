package com.example.spotistats.domain.useCases

import com.example.spotistats.domain.model.Album
import com.example.spotistats.domain.model.TopAlbum
import com.example.spotistats.domain.model.UserTopTracks
import javax.inject.Inject

class CalculateTopAlbumsUseCase @Inject constructor() {
    operator fun invoke(userTopTracks: UserTopTracks): List<TopAlbum> {
        if (userTopTracks.items.isEmpty()) {
            return emptyList()
        }

        val albumsCount = mutableMapOf<String, Pair<Album, Int>>()

        userTopTracks.items.forEach { track ->
            val album = track.album
            albumsCount.compute(album.id) { _, pair ->
                if (pair == null) {
                    Pair(album, 1)
                } else {
                    Pair(album, pair.second + 1)
                }
            }
        }

        return albumsCount.values
            .map { pair -> TopAlbum(pair.first, pair.second) }
            .sortedByDescending { topAlbum -> topAlbum.trackCount }
            .take(10)
    }
}