package com.example.spotistats.viewModels

import com.example.spotistats.domain.useCases.CalculateTopAlbumsUseCase
import com.example.spotistats.domain.useCases.PlaybackUseCase
import com.example.spotistats.domain.useCases.TopContentUseCase
import com.example.spotistats.domain.useCases.UserUseCase
import com.example.spotistats.presentation.main.MainViewModel
import com.example.spotistats.util.MainCoroutineRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import com.example.spotistats.domain.model.TimeRange
import com.example.spotistats.util.FakeFactory

class MainViewModelTest {
    private lateinit var calculateTopAlbumsUseCase: CalculateTopAlbumsUseCase
    private lateinit var topContentUseCase: TopContentUseCase
    private lateinit var playbackUseCase: PlaybackUseCase
    private lateinit var userUseCase: UserUseCase
    private lateinit var mainViewModel: MainViewModel
    
    
    @get:Rule
    val mainDispatcherRule = MainCoroutineRule()
    
    @Before
    fun setup(){
        calculateTopAlbumsUseCase = mockk()
        topContentUseCase = mockk()
        playbackUseCase = mockk()
        userUseCase = mockk()
        mainViewModel = MainViewModel(
            playbackUseCase = playbackUseCase,
            userUseCase = userUseCase,
            topContentUseCase = topContentUseCase,
            calculateTopAlbumsUseCase = calculateTopAlbumsUseCase
        )
    }

    @Test
    fun `loadStats should call all use cases and update uiState`() = runTest {
        coEvery { playbackUseCase.getRecentlyPlayed() } returns mockk()
        coEvery { userUseCase.getUserProfile() } returns mockk()
        coEvery { playbackUseCase.getCurrentlyPlaying() } returns mockk()
        coEvery { topContentUseCase.getUserTopArtists(TimeRange.SHORT) } returns mockk()
        coEvery { topContentUseCase.getUserTopTracks(TimeRange.SHORT) } returns mockk()
        coEvery { calculateTopAlbumsUseCase(any()) } returns mockk()

        mainViewModel.loadStats()
        val state = mainViewModel.uiState.value
        assertEquals(false, state.isLoading)
        coVerify { playbackUseCase.getRecentlyPlayed() }
        coVerify { userUseCase.getUserProfile() }
        coVerify { topContentUseCase.getUserTopArtists(TimeRange.SHORT) }
        coVerify { topContentUseCase.getUserTopTracks(TimeRange.SHORT) }
        coVerify { playbackUseCase.getCurrentlyPlaying() }
        coVerify { calculateTopAlbumsUseCase(any()) }
    }

    @Test
    fun `loadStats should return exceptions and update uiState`() = runTest {
        coEvery { playbackUseCase.getRecentlyPlayed() } throws RuntimeException("error")
        coEvery { playbackUseCase.getCurrentlyPlaying() } returns mockk(relaxed = true)
        coEvery { userUseCase.getUserProfile() } returns mockk(relaxed = true)
        coEvery { topContentUseCase.getUserTopArtists(TimeRange.SHORT) } returns mockk(relaxed = true)
        coEvery { topContentUseCase.getUserTopTracks(TimeRange.SHORT) } returns mockk(relaxed = true)
        coEvery { calculateTopAlbumsUseCase(any()) } returns mockk(relaxed = true)
        mainViewModel.loadStats()
        val state = mainViewModel.uiState.value
        assertEquals(false, state.isLoading)
        assertEquals("error", state.errorMessage)
    }

    @Test
    fun `getRecentlyPlayed should update uiState when success`() = runTest {
        val fakeTracks = FakeFactory.createFakeRecentlyPlayed()
        coEvery { playbackUseCase.getRecentlyPlayed() } returns fakeTracks
        mainViewModel.getRecentlyPlayed()
        val state = mainViewModel.uiState.value
        assertEquals(fakeTracks,state.recentlyPlayed)
    }


    @Test
    fun `getRecentlyPlayed should update uiState when error`() = runTest {
        coEvery { playbackUseCase.getRecentlyPlayed() } throws Exception("error")
        mainViewModel.getRecentlyPlayed()
        val state = mainViewModel
        assertEquals("error", state.uiState.value.errorMessage)
    }

    @Test
    fun `getUserProfile should update uiState when success`() = runTest{
        val fakeProfile = FakeFactory.createFakeUserProfile()
        coEvery { userUseCase.getUserProfile() } returns fakeProfile
        mainViewModel.getUserProfile()
        val state = mainViewModel.uiState.value
        assertEquals(fakeProfile, state.userProfile)
    }

    @Test
    fun `getUserProfile should return update uiState when error`() = runTest {
        coEvery { userUseCase.getUserProfile() } throws Exception("error")
        mainViewModel.getUserProfile()
        val state = mainViewModel.uiState.value
        assertEquals("error",state.errorMessage)
    }

    @Test
    fun `getCurrentlyPlaying should update uiState when success`() = runTest {
        val fakeTracks = FakeFactory.createFakeCurrentlyPlaying()
        coEvery { playbackUseCase.getCurrentlyPlaying() } returns fakeTracks
        mainViewModel.getCurrentlyPlaying()
        val state = mainViewModel.uiState.value
        assertEquals(fakeTracks, state.currentlyPlaying)
    }

    @Test
    fun `getCurrentlyPlaying should update uiState when error`() = runTest {
        coEvery { playbackUseCase.getCurrentlyPlaying() } throws Exception("error")
        mainViewModel.getCurrentlyPlaying()
        val state = mainViewModel.uiState.value
        assertEquals("error", state.errorMessage)
        assertEquals(null,state.currentlyPlaying)
    }

    @Test
    fun `getUserTopArtists should update uiState when success`() = runTest{
        val fakeArtists = FakeFactory.createFakeUserTopArtists()
        coEvery { topContentUseCase.getUserTopArtists(TimeRange.SHORT) } returns fakeArtists
        mainViewModel.getUserTopArtists()
        val state = mainViewModel.uiState.value
        assertEquals(fakeArtists, state.topArtists)
    }

    @Test
    fun `getUserTopArtists should update uiState when error`() = runTest{
        coEvery { topContentUseCase.getUserTopArtists(TimeRange.SHORT) } throws Exception("error")
        mainViewModel.getUserTopArtists()
        val state = mainViewModel.uiState.value
        assertEquals("error",state.errorMessage)
    }

    @Test
    fun `getUserTopTracks should update uiState when success`() = runTest{
        val fakeTracks = FakeFactory.createFakeUserTopTracks()

        coEvery { topContentUseCase.getUserTopTracks(TimeRange.SHORT) } returns fakeTracks
        mainViewModel.getUserTopTracks()
        val state = mainViewModel.uiState.value
        assertEquals(fakeTracks, state.topTracks)
    }

    @Test
    fun `getUserTopTracks should update uiState when error`() = runTest{
        coEvery { topContentUseCase.getUserTopTracks(TimeRange.SHORT) } throws Exception("error")
        mainViewModel.getUserTopTracks()
        val state = mainViewModel.uiState.value
        assertEquals("error", state.errorMessage)
    }


    @Test
    fun `calculatedUserTopAlbums should update uiState`() = runTest{
        val fakeTracks = FakeFactory.createFakeUserTopTracks()
        val fakeTopAlbums = FakeFactory.createFakeTopAlbums()

        coEvery { topContentUseCase.getUserTopTracks(TimeRange.SHORT) } returns fakeTracks
        coEvery { calculateTopAlbumsUseCase(fakeTracks) } returns fakeTopAlbums
        mainViewModel.getUserTopTracks()
        mainViewModel.calculatedUserTopAlbums()
        val state = mainViewModel.uiState.value
        assertEquals(fakeTopAlbums, state.topAlbums)
    }




    }

