package com.practicum.playlistmaker.settings.domain.Impl

import com.practicum.playlistmaker.settings.domain.SettingsInteractor
import com.practicum.playlistmaker.settings.domain.SettingsRepository

class SettingsInteractorImpl(private val themeSwitchRepository: SettingsRepository) :
    SettingsInteractor {
    override fun switch(isDarkModeOn: Boolean) {
        themeSwitchRepository.switchTheme(isDarkModeOn)
    }

    override fun isDarkModeOn(): Boolean {
        return themeSwitchRepository.isDarkModeOn()
    }

    override fun applyCurrentTheme() {
        themeSwitchRepository.applyCurrentTheme()
    }
}