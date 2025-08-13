package com.example.spotistats.viewModels

import com.example.spotistats.domain.useCases.CalculateTopAlbumsUseCase
import com.example.spotistats.domain.useCases.TopContentUseCase
import com.example.spotistats.presentation.stats.ContentType
import com.example.spotistats.presentation.stats.StatsViewModel
import com.example.spotistats.presentation.stats.TimeRange
import com.example.spotistats.util.FakeFactory
import com.example.spotistats.domain.model.TimeRange as DomainTimeRange
import com.example.spotistats.util.MainCoroutineRule
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

class StatsViewModelTest {
    private lateinit var topContentUseCase: TopContentUseCase
    private lateinit var calculateTopAlbumsUseCase: CalculateTopAlbumsUseCase
    private lateinit var statsViewModel: StatsViewModel




    @get:Rule
    val mainDispatcherRule = MainCoroutineRule()

    @Before
    fun setup(){
        topContentUseCase = mockk(relaxed = true)
        calculateTopAlbumsUseCase = mockk()
        statsViewModel = spyk(
            StatsViewModel(
                topContentUseCase = topContentUseCase,
                calculateTopAlbumsUseCase = calculateTopAlbumsUseCase
            )
        )
    }

    @Test
    fun `UpdateContentType should update uiState and call loadStats`(){
        val fakeContentType = ContentType.TRACKS
        coEvery { statsViewModel.loadStats() } just Runs
        statsViewModel.updateContentType(fakeContentType)
        val state = statsViewModel.uiState.value
        assertEquals(fakeContentType,state.selectedContentType)
        coVerify { statsViewModel.loadStats() }
    }

    @Test
    fun `updateTimeRange should update uiState and call loadStats`() = runTest{
        val fakeTimeRange = TimeRange.SHORT
        coEvery { statsViewModel.loadStats() } just Runs
        statsViewModel.updateTimeRange(fakeTimeRange)
        val state = statsViewModel.uiState.value
        assertEquals(fakeTimeRange,state.selectedTimeRange)
    }

    @Test
    fun `loadStats should call all use cases and update uiState`() = runTest{
        val fakeTracks = FakeFactory.createFakeUserTopTracks()
        val fakeContentType = ContentType.TRACKS
        val fakeTimeRange = TimeRange.SHORT
        coEvery { statsViewModel.updateContentType(fakeContentType) } just Runs
        coEvery { statsViewModel.updateTimeRange(fakeTimeRange) } just Runs
        coEvery { topContentUseCase.getUserTopTracks(DomainTimeRange.SHORT) } returns fakeTracks
        statsViewModel.updateContentType(fakeContentType)
        statsViewModel.updateTimeRange(fakeTimeRange)
        statsViewModel.loadStats()
        val state = statsViewModel.uiState.value
        assertEquals(fakeTracks,state.topTracks)
        assertEquals(false,state.isLoading)
    }

    @Test
    fun `loadStats should return exceptions and update uiState`() = runTest{
        val fakeContentType = ContentType.TRACKS
        val fakeTimeRange = TimeRange.SHORT
        coEvery { statsViewModel.updateContentType(fakeContentType) } just Runs
        coEvery { statsViewModel.updateTimeRange(fakeTimeRange) } just Runs
        coEvery { topContentUseCase.getUserTopTracks(DomainTimeRange.SHORT) } throws Exception("error")
        statsViewModel.updateContentType(fakeContentType)
        statsViewModel.updateTimeRange(fakeTimeRange)
        statsViewModel.loadStats()
        val state = statsViewModel.uiState.value
        assertEquals("error",state.errorMessage)
        assertEquals(false,state.isLoading)
    }


    }