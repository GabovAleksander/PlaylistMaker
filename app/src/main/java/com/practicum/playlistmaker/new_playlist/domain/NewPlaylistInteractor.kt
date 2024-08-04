package com.practicum.playlistmaker.new_playlist.domain

import com.practicum.playlistmaker.new_playlist.domain.models.Playlist

interface NewPlaylistInteractor {

    suspend fun create(playlist: Playlist)

}