package com.practicum.playlistmaker.media.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val playlistId: Int?,
    val name: String,
    val description: String,
    val cover: String?,
)

data class PlaylistWithCountTracks(
    val playlistId: Int?,
    val name: String,
    val description: String,
    val cover: String?,
    val tracksCount: Int,
)