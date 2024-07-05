package com.practicum.playlistmaker.settings.preferences

import android.content.SharedPreferences

class SharedPreferencesThemeStorage(private val preferences: SharedPreferences) : ThemeStorage  {
    override fun switch(darkThemeEnabled: Boolean) {
        preferences
            .edit()
            .putBoolean(ThemeStorage.DARK, darkThemeEnabled)
            .apply()
    }

    override fun isDarkModeOn(): Boolean {
        return preferences.getBoolean(ThemeStorage.DARK, false)
    }


}
