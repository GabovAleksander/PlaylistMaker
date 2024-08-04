package com.practicum.playlistmaker.media.domain

import com.practicum.playlistmaker.new_playlist.domain.models.Playlist
import com.practicum.playlistmaker.search.domain.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistsInteractor {
    fun getPlaylists(): Flow<List<Playlist>>
    fun isTrackAlreadyExists(playlist: Playlist, track: Track): Boolean
    suspend fun addTrackToPlaylist(playlist: Playlist, track: Track)
}