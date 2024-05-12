package com.practicum.playlistmaker.search.ui

import com.practicum.playlistmaker.player.domain.TrackDto
import com.practicum.playlistmaker.search.domain.Track

sealed interface SearchState {
    object Loading : SearchState

    object NothingFound : SearchState

    data class Success(
        val tracks: ArrayList<Track>
    ) : SearchState

    data class ShowHistory(
        val tracks: ArrayList<Track>
    ) : SearchState

    data class Error(
        val message: String
    ) : SearchState
}