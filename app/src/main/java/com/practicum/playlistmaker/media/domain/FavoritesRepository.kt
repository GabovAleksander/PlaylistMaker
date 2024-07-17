package com.practicum.playlistmaker.media.domain

import com.practicum.playlistmaker.search.domain.Track
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {
    suspend fun saveTrack(track: Track)
    suspend fun deleteTrack(trackId: Int)
    fun getFavoritesTracks(): Flow<List<Track>>
    fun isFavorite(trackId: Int): Flow<Boolean>
}