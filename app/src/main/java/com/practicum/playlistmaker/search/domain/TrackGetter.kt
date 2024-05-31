package com.practicum.playlistmaker.search.domain

import android.content.Intent
import com.practicum.playlistmaker.player.domain.TrackDto
import com.practicum.playlistmaker.search.domain.Track

interface TrackGetter {
    fun getTrack(key: String, intent: Intent): Track
}