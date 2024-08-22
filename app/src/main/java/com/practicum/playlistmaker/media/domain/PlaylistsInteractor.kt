package com.practicum.playlistmaker.media.domain

import android.net.Uri
import com.practicum.playlistmaker.new_playlist.domain.models.Playlist
import com.practicum.playlistmaker.search.domain.Track
import java.net.URI

interface PlaylistsInteractor {
    suspend fun createPlaylist(playlistName: String, playlistDescription: String, imageUri: Uri?)

    suspend fun addTrack(track: Track, playlistId: Int)

    suspend fun isTrackAlreadyExists(trackId: Int, playlistId: Int): Boolean

    suspend fun getPlaylist(playlistId: Int): Playlist

    suspend fun getPlaylistTracks(playlistId: Int): List<Track>

    suspend fun getPlaylists(): List<Playlist>

    suspend fun updatePlaylist(playlistId: Int, playlistName: String, playlistDescription: String, imageUri: Uri?)

    suspend fun deleteTrack(trackId: Int, playlistId: Int)

    suspend fun deletePlaylist(playlist: Playlist)
}