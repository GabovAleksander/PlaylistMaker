package com.practicum.playlistmaker

import android.app.Application
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate

class App:Application() {
    var darkTheme: Boolean = false

    override fun onCreate() {
        super.onCreate()

        darkTheme = when(applicationContext.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK){
            Configuration.UI_MODE_NIGHT_NO -> false
            else -> true
        }

        val themeSP = getSharedPreferences(THEME, MODE_PRIVATE)
        val getedThemeSP = themeSP.getBoolean(DARK, darkTheme)
        switchTheme(getedThemeSP)
    }

    fun switchTheme(darkThemeEnabled: Boolean)
    {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
    companion object{
        private const val THEME = "theme"
        private const val DARK = "dark"
    }
}