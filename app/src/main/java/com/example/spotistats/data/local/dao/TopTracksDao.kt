package com.example.spotistats.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.spotistats.data.local.entity.TopArtistEntity
import com.example.spotistats.data.local.entity.TopTrackEntity

@Dao
interface TopTracksDao{
    @Query("SELECT * FROM top_tracks WHERE timeRange = :timeRange ORDER BY position ASC")
    suspend fun getTopTracks(timeRange: String):List<TopTrackEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTopTracks(tracks:List<TopTrackEntity>)

    @Query("DELETE FROM top_tracks WHERE timeRange = :timeRange")
    suspend fun clearTopTracks(timeRange:String)
}