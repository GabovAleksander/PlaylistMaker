package com.practicum.playlistmaker.search.domain

import com.practicum.playlistmaker.Resource
import com.practicum.playlistmaker.player.domain.TrackDto
import kotlinx.coroutines.flow.Flow


interface TracksRepository {
    fun searchTracks(query: String): Flow<Resource<ArrayList<Track>>>
    fun addTrackToHistory(track: Track)
    fun clearHistory()
    fun getHistory(): ArrayList<Track>
}