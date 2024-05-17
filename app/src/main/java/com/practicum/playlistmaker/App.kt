package com.practicum.playlistmaker

import android.app.Application
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.settings.domain.SettingsInteractor

class App:Application(){
    lateinit var themeSwitcherInteractor: SettingsInteractor
    override fun onCreate() {
        super.onCreate()
        themeSwitcherInteractor = Creator.provideThemeSwitchInteractor(this)
        themeSwitcherInteractor.applyCurrentTheme()
    }
}
