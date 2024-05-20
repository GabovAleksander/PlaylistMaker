package com.practicum.playlistmaker.settings.preferences

import android.content.SharedPreferences
import com.practicum.playlistmaker.Constants

interface ThemeStorage {
    fun switch(darkThemeEnabled: Boolean)
    fun isDarkModeOn(): Boolean
}