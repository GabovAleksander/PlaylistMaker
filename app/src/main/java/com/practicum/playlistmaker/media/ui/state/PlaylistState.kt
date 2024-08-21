package com.practicum.playlistmaker.media.ui.state

import com.practicum.playlistmaker.new_playlist.domain.models.Playlist
import com.practicum.playlistmaker.search.domain.Track

sealed interface PlaylistState {

    data class PlaylistInfo(
        val playlist: Playlist
    ) : PlaylistState

    data class PlaylistTracks(
        val tracks: List<Track>
    ) : PlaylistState

}