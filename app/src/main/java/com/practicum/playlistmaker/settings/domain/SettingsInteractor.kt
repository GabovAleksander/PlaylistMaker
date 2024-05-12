package com.practicum.playlistmaker.settings.domain

interface SettingsInteractor {
    fun switch(isDarkModeOn: Boolean)
    fun isDarkModeOn(): Boolean
    fun applyCurrentTheme()
}