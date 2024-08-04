package com.practicum.playlistmaker.new_playlist.domain.impl

import com.practicum.playlistmaker.media.domain.PlaylistsRepository
import com.practicum.playlistmaker.new_playlist.domain.NewPlaylistInteractor
import com.practicum.playlistmaker.new_playlist.domain.models.Playlist

class NewPlaylistInteractorImpl (
    private val repository: PlaylistsRepository,
) : NewPlaylistInteractor {

    override suspend fun create(playlist: Playlist) {
        repository.createPlaylist(playlist)
    }
}