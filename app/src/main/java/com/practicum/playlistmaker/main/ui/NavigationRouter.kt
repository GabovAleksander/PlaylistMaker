package com.practicum.playlistmaker.main.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.media.ui.MediaActivity
import com.practicum.playlistmaker.search.ui.SearchActivity
import com.practicum.playlistmaker.settings.ui.SettingsActivity

class NavigationRouter(private val activity: AppCompatActivity) {

    fun toSearch() {
        val serachIntent = Intent(activity, SearchActivity::class.java)
        activity.startActivity(serachIntent)
    }

    fun toLibrary() {
        val libraryIntent = Intent(activity, MediaActivity::class.java)
        activity.startActivity(libraryIntent)
    }

    fun toSettings() {
        val settingsIntent = Intent(activity, SettingsActivity::class.java)
        activity.startActivity(settingsIntent)
    }
}