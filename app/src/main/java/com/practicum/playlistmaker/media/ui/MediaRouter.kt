package com.practicum.playlistmaker.media.ui

import androidx.appcompat.app.AppCompatActivity

class MediaRouter(private val activity : AppCompatActivity) {

    fun goBack() {
        activity.finish()
    }
}