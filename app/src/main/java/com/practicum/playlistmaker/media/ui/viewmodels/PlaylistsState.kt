package com.practicum.playlistmaker.media.ui.viewmodels

import com.practicum.playlistmaker.new_playlist.domain.models.Playlist

sealed interface PlaylistsState {

    object Empty : PlaylistsState

    data class Playlists(
        val playlists: List<Playlist>
    ) : PlaylistsState

    data class AddTrackResult(
        val isAdded: Boolean,
        val playlistName: String
    ) : PlaylistsState

}