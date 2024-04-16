package com.practicum.playlistmaker.domain

import android.content.Intent
import com.practicum.playlistmaker.domain.models.Track

interface TrackGetter {
    fun getTrack(key: String, intent: Intent) : Track
}