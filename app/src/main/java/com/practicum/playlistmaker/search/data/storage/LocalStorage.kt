package com.practicum.playlistmaker.search.data.storage

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.Constants
import com.practicum.playlistmaker.search.data.ILocalStorage
import com.practicum.playlistmaker.search.domain.Track
import com.practicum.playlistmaker.search.model.mapToTrackDto

class LocalStorage(private val preferences: SharedPreferences) :
    ILocalStorage {

    override fun addToHistory(track: Track) {

        val searchedTracks = getHistory().map { it.mapToTrackDto() } as MutableList
        if (searchedTracks.contains(track.mapToTrackDto())) {
            searchedTracks.remove(track.mapToTrackDto())
        }
        searchedTracks.add(Constants.INDEX_FIRST, track.mapToTrackDto())
        if (searchedTracks.size > Constants.HISTORY_SIZE) {
            searchedTracks.removeLast()
        }

        val json = Gson().toJson(searchedTracks)
        preferences
            .edit()
            .putString(Constants.HISTORY_KEY, json)
            .apply()
    }


    override fun clearHistory() {
        preferences
            .edit()
            .clear()
            .apply()
    }

    override fun getHistory(): ArrayList<Track> {
        val json =
            preferences.getString(Constants.HISTORY_KEY, null) ?: return arrayListOf()
        return Gson().fromJson(json, object : TypeToken<ArrayList<Track>>() {}.type)
    }
}