package com.example.spotistats.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.spotistats.data.local.entity.RecentlyPlayedTrackEntity

@Dao
interface RecentlyPlayedDao {
    @Query("SELECT * FROM recently_played ORDER BY position ASC")
    suspend fun getAll(): List<RecentlyPlayedTrackEntity>

    @Query("DELETE FROM recently_played")
    suspend fun clearAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<RecentlyPlayedTrackEntity>)
}
