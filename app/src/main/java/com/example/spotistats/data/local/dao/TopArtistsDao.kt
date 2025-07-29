package com.example.spotistats.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.spotistats.data.local.entity.TopArtistEntity
import com.example.spotistats.presentation.screen.authorization.StatsViewModel

@Dao
interface TopArtistsDao{
    @Query("SELECT * FROM top_artist WHERE timeRange = :timeRange ORDER BY position ASC")
    suspend fun getTopArtists(timeRange: String):List<TopArtistEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTopArtists(artists: kotlin.collections.List<com.example.spotistats.data.local.entity.TopArtistEntity>)

    @Query("DELETE FROM top_artist WHERE timeRange = :timeRange")
    suspend fun clearTopArtists(timeRange:String)
}