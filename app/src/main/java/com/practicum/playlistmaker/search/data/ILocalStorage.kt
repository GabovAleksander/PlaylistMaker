package com.practicum.playlistmaker.search.data

import com.practicum.playlistmaker.search.domain.Track

interface ILocalStorage {
    fun addToHistory(track: Track)
    fun clearHistory()
    fun getHistory(): List<Track>
}