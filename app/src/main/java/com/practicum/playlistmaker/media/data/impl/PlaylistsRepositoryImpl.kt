package com.practicum.playlistmaker.media.data.impl

import com.practicum.playlistmaker.media.data.db.DataBase
import com.practicum.playlistmaker.media.data.db.entity.DataMapper
import com.practicum.playlistmaker.media.data.db.entity.PlaylistEntity
import com.practicum.playlistmaker.media.domain.PlaylistsRepository
import com.practicum.playlistmaker.new_playlist.domain.models.Playlist
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaylistsRepositoryImpl (
    private val database: DataBase,
    private val playlistMapper: DataMapper,
    ) : PlaylistsRepository {

        override suspend fun createPlaylist(playlist: Playlist) {
            database
                .playlistsDao()
                .insertPlaylist(playlistMapper.map(playlist))
        }

        override suspend fun deletePlaylist(playlist: Playlist) {
            database
                .playlistsDao()
                .deletePlaylist(playlistMapper.map(playlist))
        }

        override suspend fun updateTracks(playlist: Playlist) {
            database
                .playlistsDao()
                .updatePlaylist(playlistMapper.map(playlist))
        }

        override fun getSavedPlaylists(): Flow<List<Playlist>> {
            return database
                .playlistsDao()
                .getSavedPlaylists()
                .map { convertFromTrackEntity(it) }
        }

        private fun convertFromTrackEntity(playlists: List<PlaylistEntity>): List<Playlist> {
            return playlists.map { playlistMapper.map(it) }
        }
}