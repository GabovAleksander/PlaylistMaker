package com.practicum.playlistmaker.settings.data

interface LocalStorage {
    fun switch(darkThemeEnabled: Boolean)
    fun isDarkModeOn(): Boolean
}