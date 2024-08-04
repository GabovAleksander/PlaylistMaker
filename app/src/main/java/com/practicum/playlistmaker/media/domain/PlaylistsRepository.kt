package com.practicum.playlistmaker.media.domain

import com.practicum.playlistmaker.new_playlist.domain.models.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistsRepository {
    suspend fun createPlaylist(playlist: Playlist)

    suspend fun deletePlaylist(playlist: Playlist)

    suspend fun updateTracks(playlist: Playlist)

    fun getSavedPlaylists(): Flow<List<Playlist>>
}