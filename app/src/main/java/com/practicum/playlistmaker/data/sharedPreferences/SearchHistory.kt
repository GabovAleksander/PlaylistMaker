package com.practicum.playlistmaker.data.sharedPreferences

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.domain.models.Track

class SearchHistory(val preferences: SharedPreferences) {
    private val savedList = preferences.getString(HISTORY_KEY, "[]")

    class Token : TypeToken<MutableList<Track>>()

    private val list: MutableList<Track> =
        if (savedList == null) mutableListOf() else Gson().fromJson<ArrayList<Track>>(savedList, Token().type).toMutableList()

    fun loadHistory(): MutableList<Track> {
        return list.reversed().toMutableList()
    }

    fun saveTrack(track: Track) {
        var isContains = false
        for (i in list) {
            if (track.trackId.equals(i.trackId)) {
                list.remove(i)
                list.add(i)
                isContains = true
                break
            }
        }
        if (!isContains) {
            list.add(track)
            if (list.count() > HISTORY_SIZE) {
                list.removeAt(0)
            }
        }
        val addJSon = Gson().toJson(list)
        preferences.edit()
            .putString(HISTORY_KEY, addJSon)
            .apply()
    }

    fun clearHistory() {
        list.clear()
        val emptyJSon = Gson().toJson(list)
        preferences.edit()
            .putString(HISTORY_KEY, emptyJSon)
            .apply()
    }

    companion object {
        private const val HISTORY_KEY = "history"
        private const val HISTORY_SIZE = 10
    }
}
