package com.practicum.playlistmaker.media.ui.viewmodels

import com.practicum.playlistmaker.new_playlist.domain.models.Playlist

sealed class BottomSheetState(
    val content: List<Playlist> = emptyList(),
) {

    object Empty : BottomSheetState()

    class AddedNow(val playlistModel: Playlist) : BottomSheetState()

    class AddedAlready(val playlistModel: Playlist) : BottomSheetState()

    class Content(playlists: List<Playlist>) : BottomSheetState(content = playlists)
}