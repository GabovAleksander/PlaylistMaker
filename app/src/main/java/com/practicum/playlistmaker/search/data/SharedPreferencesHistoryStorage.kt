package com.practicum.playlistmaker.search.data

import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.search.domain.Track

class SharedPreferencesHistoryStorage(
    private val preferences: SharedPreferences,
    private val gson: Gson
) :
    HistoryStorage {

    override fun addToHistory(track: Track) {

        val searchedTracks = getHistory()
        searchedTracks.remove(track)
        searchedTracks.add(0, track)

        if (searchedTracks.size > HistoryStorage.HISTORY_SIZE)
            searchedTracks.removeLast()

        val json = gson.toJson(searchedTracks)
        preferences.edit { putString(HistoryStorage.HISTORY_KEY, json) }
    }

    override fun clearHistory() {
        preferences.edit { remove(HistoryStorage.HISTORY_KEY) }
    }

    override fun getHistory(): ArrayList<Track> {
        val json =
            preferences.getString(HistoryStorage.HISTORY_KEY, null) ?: return arrayListOf()
        return gson.fromJson(json, object : TypeToken<ArrayList<Track>>() {}.type)
    }


}