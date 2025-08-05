package com.example.spotistats.domain.useCase

import com.example.spotistats.domain.model.CurrentlyItem
import com.example.spotistats.domain.model.CurrentlyPlaying
import com.example.spotistats.domain.model.RecentlyPlayed
import com.example.spotistats.domain.model.Track
import com.example.spotistats.domain.repository.AuthRepository
import com.example.spotistats.domain.repository.PlaybackRepository
import com.example.spotistats.domain.useCases.PlaybackUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertEquals
import kotlin.test.assertFailsWith

class PlaybackUseCaseTest {
    private lateinit var playbackRepository:PlaybackRepository
    private lateinit var  playbackUseCase: PlaybackUseCase
    private lateinit var authRepository: AuthRepository

    @Before
    fun setup(){
        authRepository = mockk()
        playbackRepository = mockk()
        playbackUseCase = PlaybackUseCase(playbackRepository)
    }

    @Test
    fun `getCurrentlyPlaying should return currentlyPlaying track if repository returns success`() = runTest {
        val fakeCurrentlyPlaying = CurrentlyPlaying(
            is_playing = true,
            item = CurrentlyItem(
                artists = "artists",
                duration_ms = 123,
                id = "id",
                name = "name",
                imageUrl = "imageUrl"
            ),
            progress_ms = 11
        )
        coEvery { playbackRepository.getCurrentlyPlaying() } returns fakeCurrentlyPlaying
        val result = playbackUseCase.getCurrentlyPlaying()
        assertEquals(fakeCurrentlyPlaying,result)
    }

    @Test
    fun `getRecentlyPlayed should return RecentlyPlayed track if repository returns success`() = runTest {
        val fakeRecentlyPlayed = RecentlyPlayed(
            tracks = (1..10).map {
                Track(
                    album = "album",
                    artists = "artists",
                    duration_ms = 123,
                    id = "id",
                    name = "name",
                    type = "type",
                    uri = "uri",
                    imageUrl = "imageUrl"
                )
            }
        )
        coEvery { playbackRepository.getRecentlyPlayed() } returns fakeRecentlyPlayed
        val result = playbackUseCase.getRecentlyPlayed()
        assertEquals(fakeRecentlyPlayed,result)
    }

    @Test
    fun `getCurrentlyPlaying should throw exception if repository fails`() = runTest {
        coEvery { playbackRepository.getCurrentlyPlaying() } throws RuntimeException("Network error")
        assertFailsWith<RuntimeException> {
            playbackUseCase.getCurrentlyPlaying()
        }
    }


    @Test
    fun `getRecentlyPlayed should throw exception if repository fails`() = runTest {
        coEvery { playbackRepository.getRecentlyPlayed() } throws RuntimeException("Network error")
        assertFailsWith<RuntimeException> {
            playbackUseCase.getRecentlyPlayed()
        }
    }
}