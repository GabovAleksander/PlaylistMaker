package com.practicum.playlistmaker.settings.ui

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.App
import com.practicum.playlistmaker.settings.domain.SettingsInteractor


class SettingsViewModel(private val settingsInteractor: SettingsInteractor) : ViewModel() {

    fun switchTheme(isChecked: Boolean) {
        settingsInteractor.switch(isChecked)
    }

    fun isDarkThemeOn(): Boolean {
        return settingsInteractor.isDarkModeOn()
    }
}