package com.practicum.playlistmaker.media.data.impl

import com.practicum.playlistmaker.media.data.db.DataBase
import com.practicum.playlistmaker.media.data.db.entity.DataMapper
import com.practicum.playlistmaker.media.data.db.entity.TrackEntity
import com.practicum.playlistmaker.media.domain.FavoritesRepository
import com.practicum.playlistmaker.search.domain.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.Calendar

class FavoritesRepositoryImpl(
    private val database: DataBase,
    private val mapper: DataMapper,
) : FavoritesRepository {
    override suspend fun saveTrack(track: Track) {
        database
            .tracksDao()
            .addTrack(mapper.map(track, Calendar.getInstance().timeInMillis))
    }

    override suspend fun deleteTrack(trackId: Int) {
        database
            .tracksDao()
            .deleteTrack(trackId)
    }

    override fun getFavoritesTracks(): Flow<List<Track>> = flow {
        val tracks = database
            .tracksDao().getFavoriteTracks()
        emit(convertFromTrackEntity(tracks))
    }

    override fun isFavorite(trackId: Int): Flow<Boolean> = flow {
        val isInFavorite = database
            .tracksDao()
            .isFavorite(trackId)
        emit(isInFavorite)
    }

    private fun convertFromTrackEntity(tracks: List<TrackEntity>): List<Track> {
        return tracks.map { track -> mapper.map(track) }
    }
}