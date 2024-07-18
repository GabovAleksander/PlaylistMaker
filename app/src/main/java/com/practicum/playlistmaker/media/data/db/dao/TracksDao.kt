package com.practicum.playlistmaker.media.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practicum.playlistmaker.media.data.db.entity.TrackEntity


@Dao
interface TracksDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addTrack(track: TrackEntity)

    @Query("DELETE FROM favorites_tracks WHERE trackId = :trackId")
    fun deleteTrack(trackId: Int)

    @Query("SELECT * FROM favorites_tracks ORDER BY saveDate DESC;")
    fun getFavoriteTracks(): List<TrackEntity>

    @Query("SELECT EXISTS(SELECT 1 FROM favorites_tracks WHERE trackId = :trackId)")
    fun isFavorite(trackId: Int): Boolean
}