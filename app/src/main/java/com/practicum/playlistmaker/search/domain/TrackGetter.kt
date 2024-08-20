package com.practicum.playlistmaker.search.domain

import android.content.Intent

interface TrackGetter {
    fun getTrack(key: String, intent: Intent): Track
}