package com.practicum.playlistmaker.media.domain.impl

import com.practicum.playlistmaker.media.domain.FavoritesInteractor
import com.practicum.playlistmaker.media.domain.FavoritesRepository
import com.practicum.playlistmaker.search.domain.Track
import kotlinx.coroutines.flow.Flow

class FavoritesInteractorImpl(
    private val repository: FavoritesRepository,
) : FavoritesInteractor {

    override suspend fun addTrack(track: Track) {
        repository.saveTrack(track)
    }

    override suspend fun deleteTrack(trackId: Int) {
        repository.deleteTrack(trackId)
    }

    override fun getFavoritesTracks(): Flow<List<Track>> {
        return repository.getFavoritesTracks()
    }

    override fun isFavorite(trackId: Int): Flow<Boolean> {
        return repository.isFavorite(trackId)
    }
}