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
import com.example.spotistats.domain.model.TimeRange
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
        coEvery { topContentRepository.getUserTopArtists(TimeRange.SHORT) } returns fakeTopArtists
        val result = topContentUseCase.getUserTopArtists(TimeRange.SHORT)
        assertEquals(fakeTopArtists,result)
    }

    @Test
    fun `getUserTopArtistsShort should return exception when repository returns error`() = runTest{
        coEvery { topContentRepository.getUserTopArtists(TimeRange.SHORT) } throws RuntimeException("Network error")
        assertFailsWith<RuntimeException> {
            topContentUseCase.getUserTopArtists(TimeRange.SHORT)
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
        coEvery { topContentRepository.getUserTopArtists(TimeRange.MEDIUM) } returns fakeTopArtists
        val result = topContentUseCase.getUserTopArtists(TimeRange.MEDIUM)
        assertEquals(fakeTopArtists,result)
    }

    @Test
    fun `getUserTopArtistsMedium should return exception when repository returns error`() = runTest{
        coEvery { topContentRepository.getUserTopArtists(TimeRange.MEDIUM) } throws RuntimeException("Network error")
        assertFailsWith<RuntimeException> {
            topContentUseCase.getUserTopArtists(TimeRange.MEDIUM)
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
        coEvery { topContentRepository.getUserTopArtists(TimeRange.LONG) } returns fakeTopArtists
        val result = topContentUseCase.getUserTopArtists(TimeRange.LONG)
        assertEquals(fakeTopArtists,result)
    }

    @Test
    fun `getUserTopArtistsLong should return exception when repository returns error`() = runTest{
        coEvery { topContentRepository.getUserTopArtists(TimeRange.LONG) } throws RuntimeException("Network error")
        assertFailsWith<RuntimeException> {
            topContentUseCase.getUserTopArtists(TimeRange.LONG)
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
        coEvery { topContentRepository.getUserTopTracks(TimeRange.SHORT) } returns fakeTopTracks
        val result = topContentUseCase.getUserTopTracks(TimeRange.SHORT)
        assertEquals(fakeTopTracks,result)
        }

    @Test
    fun `getUserTopTracksShort should return exception when repository returns error`() = runTest{
        coEvery { topContentRepository.getUserTopTracks(TimeRange.SHORT) } throws RuntimeException("Network error")
        assertFailsWith<RuntimeException> {
            topContentUseCase.getUserTopTracks(TimeRange.SHORT)
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
        coEvery { topContentRepository.getUserTopTracks(TimeRange.MEDIUM) } returns fakeTopTracks
        val result = topContentUseCase.getUserTopTracks(TimeRange.MEDIUM)
        assertEquals(fakeTopTracks,result)
    }

    @Test
    fun `getUserTopTracksMedium should return exception when repository returns error`() = runTest{
        coEvery { topContentRepository.getUserTopTracks(TimeRange.MEDIUM) } throws RuntimeException("Network error")
        assertFailsWith<RuntimeException> {
            topContentUseCase.getUserTopTracks(TimeRange.MEDIUM)
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
        coEvery { topContentRepository.getUserTopTracks(TimeRange.LONG) } returns fakeTopTracks
        val result = topContentUseCase.getUserTopTracks(TimeRange.LONG)
        assertEquals(fakeTopTracks,result)
    }

    @Test
    fun `getUserTopTracksLong should return exception when repository returns error`() = runTest{
        coEvery { topContentRepository.getUserTopTracks(TimeRange.LONG) } throws RuntimeException("Network error")
        assertFailsWith<RuntimeException> {
            topContentUseCase.getUserTopTracks(TimeRange.LONG)
        }
    }

    }