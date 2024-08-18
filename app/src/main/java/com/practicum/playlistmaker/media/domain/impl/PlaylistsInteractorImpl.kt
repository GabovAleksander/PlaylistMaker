package com.practicum.playlistmaker.media.domain.impl

import android.net.Uri
import com.practicum.playlistmaker.media.domain.PlaylistsInteractor
import com.practicum.playlistmaker.media.domain.PlaylistsRepository
import com.practicum.playlistmaker.new_playlist.domain.models.Playlist
import com.practicum.playlistmaker.search.domain.Track
import java.net.URI

class PlaylistsInteractorImpl(
    private val repository: PlaylistsRepository,
) : PlaylistsInteractor {

    override suspend fun createPlaylist(playlistName: String, playlistDescription: String, imageUri: Uri) =
        repository.createPlaylist(playlistName, playlistDescription, imageUri)

    override suspend fun addTrack(track: Track, playlistId: Int) =
        repository.addTrack(track, playlistId)

    override suspend fun isTrackAlreadyExists(trackId : Int, playlistId: Int) : Boolean=
        repository.isTrackAlreadyExists(trackId, playlistId)

    override suspend fun getPlaylists(): List<Playlist> =
        repository.getPlaylists()

    override suspend fun getPlaylist(playlistId: Int) : Playlist =
        repository.getPlaylist(playlistId)

    override suspend fun getPlaylistTracks(playlistId: Int): List<Track> =
        repository.getPlaylistTracks(playlistId)

    override suspend fun updatePlaylist(playlistId: Int, playlistName: String, playlistDescription: String, imageUri: Uri) {
        repository.updatePlaylist(playlistId, playlistName, playlistDescription, imageUri)
    }
}