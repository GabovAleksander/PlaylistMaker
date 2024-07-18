package com.practicum.playlistmaker.media.ui.viewmodels

import com.practicum.playlistmaker.search.domain.Track

sealed interface FavoritesState {
    object Empty : FavoritesState

    data class FavoritesTracks(
        val tracks: List<Track>,
    ) : FavoritesState
}