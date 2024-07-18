package com.practicum.playlistmaker.media.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.practicum.playlistmaker.media.data.db.dao.TracksDao
import com.practicum.playlistmaker.media.data.db.entity.TrackEntity

@Database(version = 1, entities = [TrackEntity::class])
abstract class DataBase : RoomDatabase() {

    abstract fun tracksDao(): TracksDao
}