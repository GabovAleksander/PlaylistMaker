package com.practicum.playlistmaker

import android.app.Application
import com.markodevcic.peko.PermissionRequester
import com.practicum.playlistmaker.di.dataModule
import com.practicum.playlistmaker.di.interactorModule
import com.practicum.playlistmaker.di.repositoryModule
import com.practicum.playlistmaker.di.viewModelModule
import com.practicum.playlistmaker.settings.domain.SettingsInteractor
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.java.KoinJavaComponent.inject


class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@App)
            modules(
                listOf(
                    dataModule,
                    repositoryModule,
                    interactorModule,
                    viewModelModule
                )
            )
        }
        PermissionRequester.initialize(applicationContext)
        val settingsInteractor: SettingsInteractor by inject()
        //settingsInteractor.isDarkModeOn()
        settingsInteractor.applyCurrentTheme()
    }
}
