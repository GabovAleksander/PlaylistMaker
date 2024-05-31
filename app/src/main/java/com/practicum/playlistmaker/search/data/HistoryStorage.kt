package com.practicum.playlistmaker.search.data

import com.practicum.playlistmaker.search.domain.Track

interface HistoryStorage {
    fun addToHistory(track: Track)
    fun clearHistory()
    fun getHistory(): ArrayList<Track>
}