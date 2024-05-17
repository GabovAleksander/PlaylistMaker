package com.practicum.playlistmaker.search.ui

import android.app.Activity
import android.content.Intent
import com.practicum.playlistmaker.Constants
import com.practicum.playlistmaker.player.domain.TrackDto
import com.practicum.playlistmaker.player.ui.PlayerActivity
import com.practicum.playlistmaker.search.domain.Track

class Router(private val activity: Activity) {

    fun goBack() {
        activity.finish()
    }

    fun openPlayer(track: Track) {
        val intent = Intent(activity, PlayerActivity::class.java).apply {
             putExtra(Constants.TRACK, track)
        }
        activity.startActivity(intent)
    }
}
