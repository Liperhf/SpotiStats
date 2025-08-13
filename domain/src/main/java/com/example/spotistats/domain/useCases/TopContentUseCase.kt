package com.example.spotistats.domain.useCases

import com.example.spotistats.domain.model.UserTopArtists
import com.example.spotistats.domain.model.UserTopTracks
import com.example.spotistats.domain.repository.TopContentRepository
import com.example.spotistats.domain.model.TimeRange
import javax.inject.Inject

class TopContentUseCase @Inject constructor(
    private val topContentRepository: TopContentRepository
) {
    
    suspend fun getUserTopArtists(timeRange: TimeRange = TimeRange.SHORT): UserTopArtists {
        return topContentRepository.getUserTopArtists(timeRange)
    }

    suspend fun getUserTopTracks(timeRange: TimeRange = TimeRange.SHORT): UserTopTracks {
        return topContentRepository.getUserTopTracks(timeRange)
    }
}