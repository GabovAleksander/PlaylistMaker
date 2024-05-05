package com.practicum.playlistmaker.data

import android.content.Intent
import com.google.gson.Gson
import com.practicum.playlistmaker.domain.TrackGetter
import com.practicum.playlistmaker.domain.models.Track

class TrackGetterImpl:TrackGetter {
    override fun getTrack(key: String, intent: Intent): Track {
        val gson=Gson()
        val json=intent.getStringExtra(key)
        return gson.fromJson(json, Track::class.java)
    }
}