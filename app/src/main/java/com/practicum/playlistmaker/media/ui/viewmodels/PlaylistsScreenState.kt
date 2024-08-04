package com.practicum.playlistmaker.media.ui.viewmodels

import com.practicum.playlistmaker.new_playlist.domain.models.Playlist

sealed class PlaylistsScreenState {

    object Empty : PlaylistsScreenState()

    class Content(val playlists: List<Playlist>) : PlaylistsScreenState()
}