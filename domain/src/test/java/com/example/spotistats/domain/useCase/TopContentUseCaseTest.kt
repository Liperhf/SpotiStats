package com.example.spotistats.domain.useCase

import com.example.spotistats.domain.model.Album
import com.example.spotistats.domain.model.ArtistX
import com.example.spotistats.domain.model.Image
import com.example.spotistats.domain.model.UserTopArtists
import com.example.spotistats.domain.model.UserTopArtistsItem
import com.example.spotistats.domain.model.UserTopTracks
import com.example.spotistats.domain.model.UserTopTracksItem
import com.example.spotistats.domain.repository.TopContentRepository
import com.example.spotistats.domain.useCases.TopContentUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class TopContentUseCaseTest {
    private lateinit var topContentUseCase: TopContentUseCase
    private lateinit var topContentRepository: TopContentRepository

    @Before
    fun setup(){
        topContentRepository = mockk()
        topContentUseCase = TopContentUseCase(topContentRepository)
    }

    @Test
    fun `getUserTopArtistsShort should return TopArtists when repository returns success`() = runTest{
        val artists = (1..10).map {
            UserTopArtistsItem(
                name = "artist$it",
                images = listOf(Image(
                    url = "url$it",
                    height = 1,
                    width = 1
                )),
            )
        }
        val fakeTopArtists = UserTopArtists(artists)
        coEvery { topContentRepository.getUserTopArtistsShort() } returns fakeTopArtists
        val result = topContentUseCase.getUserTopArtistsShort()
        assertEquals(fakeTopArtists,result)
    }

    @Test
    fun `getUserTopArtistsShort should return exception when repository returns error`() = runTest{
        coEvery { topContentRepository.getUserTopArtistsShort() } throws RuntimeException("Network error")
        assertFailsWith<RuntimeException> {
            topContentUseCase.getUserTopArtistsShort()
        }
    }


    @Test
    fun `getUserTopArtistsMedium should return TopArtists when repository returns success`() = runTest{
        val artists = (1..10).map {
            UserTopArtistsItem(
                name = "artist$it",
                images = listOf(Image(
                    url = "url$it",
                    height = 1,
                    width = 1
                )),
            )
        }
        val fakeTopArtists = UserTopArtists(artists)
        coEvery { topContentRepository.getUserTopArtistsMedium() } returns fakeTopArtists
        val result = topContentUseCase.getUserTopArtistsMedium()
        assertEquals(fakeTopArtists,result)
    }

    @Test
    fun `getUserTopArtistsMedium should return exception when repository returns error`() = runTest{
        coEvery { topContentRepository.getUserTopArtistsMedium() } throws RuntimeException("Network error")
        assertFailsWith<RuntimeException> {
            topContentUseCase.getUserTopArtistsMedium()
        }
    }

    @Test
    fun `getUserTopArtistsLong should return TopArtists when repository returns success`() = runTest{
        val artists = (1..10).map {
            UserTopArtistsItem(
                name = "artist$it",
                images = listOf(Image(
                    url = "url$it",
                    height = 1,
                    width = 1
                )),
            )
        }
        val fakeTopArtists = UserTopArtists(artists)
        coEvery { topContentRepository.getUserTopArtistsLong() } returns fakeTopArtists
        val result = topContentUseCase.getUserTopArtistsLong()
        assertEquals(fakeTopArtists,result)
    }

    @Test
    fun `getUserTopArtistsLong should return exception when repository returns error`() = runTest{
        coEvery { topContentRepository.getUserTopArtistsLong() } throws RuntimeException("Network error")
        assertFailsWith<RuntimeException> {
            topContentUseCase.getUserTopArtistsLong()
        }
    }


    @Test
    fun `getUserTopTracksShort should return TopTracks when repository returns success`() = runTest{
        val tracks = (1..10).map {
            UserTopTracksItem(
                album = Album(
                    artists = (1..10).map {
                        ArtistX(
                            name = "artist$it"
                        )
                    },
                    id = "id$it",
                    images = (1..10).map{
                        Image(
                            url = "url$it",
                            height = 1,
                            width = 1
                        )
                    },
                    name = "name$it"
                ),
                artists = (1..10).map{
                    ArtistX(
                        name = "artist$it"
                    )
                },
                name = "name$it"
            )
        }
        val fakeTopTracks = UserTopTracks(tracks)
        coEvery { topContentRepository.getUserTopTracksShort() } returns fakeTopTracks
        val result = topContentUseCase.getUserTopTracksShort()
        assertEquals(fakeTopTracks,result)
        }

    @Test
    fun `getUserTopTracksShort should return exception when repository returns error`() = runTest{
        coEvery { topContentRepository.getUserTopTracksShort() } throws RuntimeException("Network error")
        assertFailsWith<RuntimeException> {
            topContentUseCase.getUserTopTracksShort()
        }
    }


    @Test
    fun `getUserTopTracksMedium should return TopTracks when repository returns success`() = runTest{
        val tracks = (1..10).map {
            UserTopTracksItem(
                album = Album(
                    artists = (1..10).map {
                        ArtistX(
                            name = "artist$it"
                        )
                    },
                    id = "id$it",
                    images = (1..10).map{
                        Image(
                            url = "url$it",
                            height = 1,
                            width = 1
                        )
                    },
                    name = "name$it"
                ),
                artists = (1..10).map{
                    ArtistX(
                        name = "artist$it"
                    )
                },
                name = "name$it"
            )
        }
        val fakeTopTracks = UserTopTracks(tracks)
        coEvery { topContentRepository.getUserTopTracksMedium() } returns fakeTopTracks
        val result = topContentUseCase.getUserTopTracksMedium()
        assertEquals(fakeTopTracks,result)
    }

    @Test
    fun `getUserTopTracksMedium should return exception when repository returns error`() = runTest{
        coEvery { topContentRepository.getUserTopTracksMedium() } throws RuntimeException("Network error")
        assertFailsWith<RuntimeException> {
            topContentUseCase.getUserTopTracksMedium()
        }
    }


    @Test
    fun `getUserTopTracksLong should return TopTracks when repository returns success`() = runTest{
        val tracks = (1..10).map {
            UserTopTracksItem(
                album = Album(
                    artists = (1..10).map {
                        ArtistX(
                            name = "artist$it"
                        )
                    },
                    id = "id$it",
                    images = (1..10).map{
                        Image(
                            url = "url$it",
                            height = 1,
                            width = 1
                        )
                    },
                    name = "name$it"
                ),
                artists = (1..10).map{
                    ArtistX(
                        name = "artist$it"
                    )
                },
                name = "name$it"
            )
        }
        val fakeTopTracks = UserTopTracks(tracks)
        coEvery { topContentRepository.getUserTopTracksLong() } returns fakeTopTracks
        val result = topContentUseCase.getUserTopTracksLong()
        assertEquals(fakeTopTracks,result)
    }

    @Test
    fun `getUserTopTracksLong should return exception when repository returns error`() = runTest{
        coEvery { topContentRepository.getUserTopTracksLong() } throws RuntimeException("Network error")
        assertFailsWith<RuntimeException> {
            topContentUseCase.getUserTopTracksLong()
        }
    }

    }