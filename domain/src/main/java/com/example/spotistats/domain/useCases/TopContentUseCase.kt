package com.example.spotistats.domain.useCases

import com.example.spotistats.domain.model.UserTopArtists
import com.example.spotistats.domain.model.UserTopTracks
import com.example.spotistats.domain.repository.TopContentRepository
import javax.inject.Inject

class TopContentUseCase @Inject constructor(
    private val topContentRepository: TopContentRepository
) {

    suspend fun getUserTopArtistsShort(): UserTopArtists {
        return topContentRepository.getUserTopArtistsShort()
    }

    suspend fun getUserTopTracksShort(): UserTopTracks {
        return topContentRepository.getUserTopTracksShort()
    }

    suspend fun getUserTopArtistsMedium(): UserTopArtists {
        return topContentRepository.getUserTopArtistsMedium()
    }

    suspend fun getUserTopArtistsLong(): UserTopArtists {
        return topContentRepository.getUserTopArtistsLong()
    }

    suspend fun getUserTopTracksMedium(): UserTopTracks {
        return topContentRepository.getUserTopTracksMedium()
    }

    suspend fun getUserTopTracksLong(): UserTopTracks {
        return topContentRepository.getUserTopTracksLong()
    }


}