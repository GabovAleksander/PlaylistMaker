package com.practicum.playlistmaker.settings.data.Impl

import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.settings.data.LocalStorage
import com.practicum.playlistmaker.settings.domain.SettingsRepository

class SettingsRepositoryImpl(private val themeStorage: LocalStorage) : SettingsRepository {
    override fun switchTheme(darkThemeEnabled: Boolean) {
        themeStorage.switch(darkThemeEnabled)
        applyCurrentTheme()
    }

    override fun isDarkModeOn(): Boolean {
        return themeStorage.isDarkModeOn()
    }

    override fun applyCurrentTheme() {
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkModeOn()) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}