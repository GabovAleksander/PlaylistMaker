package com.practicum.playlistmaker.new_playlist.domain

import android.net.Uri
import com.practicum.playlistmaker.new_playlist.domain.models.Playlist
import com.practicum.playlistmaker.search.domain.Track

interface NewPlaylistInteractor {
    suspend fun createPlaylist(playlistName: String, playlistDescription: String, imageUri: Uri)

    suspend fun addTrack(track: Track, playlistId: Int)

    suspend fun isTrackAlreadyExists(trackId: Int, playlistId: Int): Boolean

    suspend fun getPlaylist(playlistId: Int): Playlist

    suspend fun getPlaylistTracks(playlistId: Int): List<Track>

    suspend fun getPlaylists(): List<Playlist>

    suspend fun updatePlaylist(playlistId: Int, playlistName: String, playlistDescription: String, imageUri: Uri)
}