package com.practicum.playlistmaker.settings.domain

interface SettingsRepository {
        fun switchTheme(darkThemeEnabled: Boolean)
        fun isDarkModeOn(): Boolean
        fun applyCurrentTheme()
}