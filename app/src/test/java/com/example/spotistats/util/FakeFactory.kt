package com.example.spotistats.util

import com.example.spotistats.domain.model.Album
import com.example.spotistats.domain.model.ArtistX
import com.example.spotistats.domain.model.CurrentlyItem
import com.example.spotistats.domain.model.CurrentlyPlaying
import com.example.spotistats.domain.model.Image
import com.example.spotistats.domain.model.RecentlyPlayed
import com.example.spotistats.domain.model.TopAlbum
import com.example.spotistats.domain.model.Track
import com.example.spotistats.domain.model.UserProfile
import com.example.spotistats.domain.model.UserTopArtists
import com.example.spotistats.domain.model.UserTopArtistsItem
import com.example.spotistats.domain.model.UserTopTracks
import com.example.spotistats.domain.model.UserTopTracksItem

class FakeFactory {
    object FakeFactory {

        fun createFakeTrack(): Track = Track(
            album = "album",
            artists = "artist",
            duration_ms = 123456,
            id = "track_id",
            name = "track name",
            type = "track",
            uri = "spotify:track:uri",
            imageUrl = "https://image.url"
        )

        fun createFakeRecentlyPlayed(count: Int = 10): RecentlyPlayed =
            RecentlyPlayed(tracks = List(count) { createFakeTrack() })

        fun createFakeUserProfile(): UserProfile = UserProfile(
            display_name = "Fake User",
            imagesUrl = "https://user.image.url"
        )

        fun createFakeImage(): Image = Image(
            height = 640,
            url = "https://album.cover.url",
            width = 640
        )

        fun createFakeArtistX(name: String = "Artist"): ArtistX = ArtistX(name = name)

        fun createFakeAlbum(): Album = Album(
            id = "album_id",
            name = "Fake Album",
            artists = listOf(createFakeArtistX()),
            images = listOf(createFakeImage())
        )

        fun createFakeUserTopTracks(count: Int = 10): UserTopTracks =
            UserTopTracks(
                items = List(count) {
                    UserTopTracksItem(
                        album = createFakeAlbum(),
                        artists = listOf(createFakeArtistX()),
                        name = "Track $it"
                    )
                }
            )

        fun createFakeTopAlbums(count: Int = 5): List<TopAlbum> =
            List(count) {
                TopAlbum(
                    album = createFakeAlbum(),
                    trackCount = it + 1
                )
            }

        fun createFakeUserTopArtists(count: Int = 10): UserTopArtists =
            UserTopArtists(
                items = List(count) {
                    UserTopArtistsItem(
                        name = "Artist $it",
                        images = listOf(createFakeImage())
                    )
                }
            )

        fun createFakeCurrentlyPlaying(): CurrentlyPlaying =
            CurrentlyPlaying(
                is_playing = true,
                item = CurrentlyItem(
                    id = "track_id",
                    name = "Current Track",
                    artists = "Artist",
                    duration_ms = 200000,
                    imageUrl = "https://image.url"
                ),
                progress_ms = 50000
            )
    }
}