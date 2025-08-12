package com.example.spotistats.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.spotistats.data.local.dao.TopArtistsDao
import com.example.spotistats.data.local.dao.TopTracksDao
import com.example.spotistats.data.local.entity.TopArtistEntity
import com.example.spotistats.data.local.entity.TopTrackEntity

@Database(entities = [TopArtistEntity::class, TopTrackEntity::class], version = 1)
abstract class AppDataBase : RoomDatabase() {
    abstract fun topArtistsDao(): TopArtistsDao
    abstract fun topTracksDao(): TopTracksDao
}