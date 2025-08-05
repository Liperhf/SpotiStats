package com.example.spotistats.domain.useCase

import com.example.spotistats.domain.model.Album
import com.example.spotistats.domain.model.UserTopTracks
import com.example.spotistats.domain.model.UserTopTracksItem
import com.example.spotistats.domain.useCases.CalculateTopAlbumsUseCase
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertEquals

class CalculatedTopAlbumsUseCaseTest {
    private lateinit var calculateTopAlbumsUseCase:CalculateTopAlbumsUseCase


    @Before
    fun setup(){
        calculateTopAlbumsUseCase = CalculateTopAlbumsUseCase()
    }

    @Test
    fun `CalculatedTopAlbums should return ten top albums if UserTopTrack is not empty and more than ten`() = runTest{
        val tracks = (1..15).map {
            UserTopTracksItem(
                album = Album(
                    artists = emptyList(),
                    id = "album_$it",
                    images = emptyList(),
                    name = "name_$it"
                ),
                artists = emptyList(),
                name = "name_$it"
            )
        }

        val fakeUserTopTracks = UserTopTracks(items = tracks)
        val result = calculateTopAlbumsUseCase(fakeUserTopTracks)
        assert(result.size == 10)
        assert(result.first().trackCount >= result.last().trackCount)
    }

    @Test
    fun `CalculatedTopAlbums should return albums if UserTopTrack is not empty and less than ten`() = runTest{
        val tracks = (1..5).map {
            UserTopTracksItem(
                album = Album(
                    artists = emptyList(),
                    id = "album_$it",
                    images = emptyList(),
                    name = "name_$it"
                ),
                artists = emptyList(),
                name = "name_$it"
            )
        }
        val fakeUserTopTracks = UserTopTracks(items = tracks)
        val result = calculateTopAlbumsUseCase(fakeUserTopTracks)
        assert(result.size == 5)
        assert(result.first().trackCount >= result.last().trackCount)
    }

    @Test
    fun `CalculatedTopAlbums should return emptyList if UserTopTrack empty`() = runTest {
        val tracks = emptyList<UserTopTracksItem>()
        val fakeUserTopTracks = UserTopTracks(items = tracks)
        val result = calculateTopAlbumsUseCase(fakeUserTopTracks)
        assertEquals(emptyList<Album>(),result)
    }
}