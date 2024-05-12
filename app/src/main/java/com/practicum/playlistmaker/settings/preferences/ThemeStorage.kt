package com.practicum.playlistmaker.settings.preferences

import android.content.SharedPreferences
import com.practicum.playlistmaker.Constants
import com.practicum.playlistmaker.settings.data.LocalStorage

class ThemeStorage(private val sharedPreferences: SharedPreferences): LocalStorage {

    override fun switch(darkThemeEnabled: Boolean) {
        sharedPreferences
            .edit()
            .putBoolean(Constants.DARK, darkThemeEnabled)
            .apply()
    }

    override fun isDarkModeOn(): Boolean {
        return sharedPreferences.getBoolean(Constants.DARK, false)
    }

}