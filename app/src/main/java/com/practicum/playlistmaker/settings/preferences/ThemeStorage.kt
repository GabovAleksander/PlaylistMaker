package com.practicum.playlistmaker.settings.preferences

interface ThemeStorage {
    fun switch(darkThemeEnabled: Boolean)
    fun isDarkModeOn(): Boolean
    companion object{
        internal const val DARK = "dark"
    }
}