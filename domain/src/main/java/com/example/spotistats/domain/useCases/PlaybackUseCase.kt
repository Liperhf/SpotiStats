package com.example.spotistats.domain.useCases

import com.example.spotistats.domain.model.CurrentlyPlaying
import com.example.spotistats.domain.model.RecentlyPlayed
import com.example.spotistats.domain.repository.PlaybackRepository
import javax.inject.Inject

class PlaybackUseCase @Inject constructor(
    private val playbackRepository: PlaybackRepository,
) {
    suspend fun getCurrentlyPlaying(): CurrentlyPlaying {
        return playbackRepository.getCurrentlyPlaying()
    }

    suspend fun getRecentlyPlayed(): RecentlyPlayed {
        return playbackRepository.getRecentlyPlayed()
    }
}