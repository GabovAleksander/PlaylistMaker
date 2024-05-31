package com.practicum.playlistmaker.settings.preferences

import android.content.SharedPreferences
import com.practicum.playlistmaker.Constants
import org.koin.core.qualifier.named
import org.koin.java.KoinJavaComponent.inject

class SharedPreferencesThemeStorage(private val preferences: SharedPreferences) : ThemeStorage  {
    override fun switch(darkThemeEnabled: Boolean) {
        preferences
            .edit()
            .putBoolean(Constants.DARK, darkThemeEnabled)
            .apply()
    }

    override fun isDarkModeOn(): Boolean {
        return preferences.getBoolean(Constants.DARK, false)
    }
}
