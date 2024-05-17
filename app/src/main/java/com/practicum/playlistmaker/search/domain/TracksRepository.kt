package com.practicum.playlistmaker.search.domain

import com.practicum.playlistmaker.Resource
import com.practicum.playlistmaker.player.domain.TrackDto


interface TracksRepository {
    fun searchTracks(query: String): Resource<List<Track>>
    fun addTrackToHistory(track: Track)
    fun clearHistory()
    fun getHistory(): ArrayList<Track>
}