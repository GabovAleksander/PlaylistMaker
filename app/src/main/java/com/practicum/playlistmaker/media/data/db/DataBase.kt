package com.practicum.playlistmaker.media.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.practicum.playlistmaker.media.data.db.dao.PlaylistDao
import com.practicum.playlistmaker.media.data.db.dao.TracksDao
import com.practicum.playlistmaker.media.data.db.entity.PlaylistEntity
import com.practicum.playlistmaker.media.data.db.entity.PlaylistsTrackEntity
import com.practicum.playlistmaker.media.data.db.entity.TrackEntity
import com.practicum.playlistmaker.media.data.db.entity.TrackPlaylistEntity

@Database(version = 2, entities = [TrackEntity::class, PlaylistEntity::class, PlaylistsTrackEntity::class, TrackPlaylistEntity::class])
abstract class DataBase : RoomDatabase() {

    abstract fun tracksDao(): TracksDao

    abstract fun playlistsDao(): PlaylistDao
}