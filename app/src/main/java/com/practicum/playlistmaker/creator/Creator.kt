package com.practicum.playlistmaker.creator

import android.content.Context
import com.practicum.playlistmaker.Constants
import com.practicum.playlistmaker.player.data.Player
import com.practicum.playlistmaker.player.data.impl.PlayerRepositoryImpl
import com.practicum.playlistmaker.player.domain.Impl.PlayerInteractorImpl
import com.practicum.playlistmaker.player.domain.PlayerInteractor
import com.practicum.playlistmaker.player.domain.PlayerRepository
import com.practicum.playlistmaker.player.domain.TrackDto
import com.practicum.playlistmaker.search.data.NetworkClient
import com.practicum.playlistmaker.search.data.impl.TracksRepositoryImpl
import com.practicum.playlistmaker.search.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.search.data.storage.LocalStorage


import com.practicum.playlistmaker.search.domain.Impl.TracksInteractorImpl
import com.practicum.playlistmaker.search.domain.Track
import com.practicum.playlistmaker.search.domain.TracksInteractor
import com.practicum.playlistmaker.search.domain.TracksRepository
import com.practicum.playlistmaker.search.model.mapToTrackDto
import com.practicum.playlistmaker.settings.data.Impl.SettingsRepositoryImpl



import com.practicum.playlistmaker.settings.domain.Impl.SettingsInteractorImpl
import com.practicum.playlistmaker.settings.domain.SettingsInteractor
import com.practicum.playlistmaker.settings.domain.SettingsRepository
import com.practicum.playlistmaker.settings.preferences.ThemeStorage

object Creator {

    private fun getTracksRepository(context: Context): TracksRepository {
        return TracksRepositoryImpl(
            RetrofitNetworkClient(context),
            LocalStorage(context.getSharedPreferences(Constants.HISTORY_TRACKS_SHARED_PREF, Context.MODE_PRIVATE))
        )
    }

    fun provideTracksInteractor(context: Context): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository(context))
    }

    private fun getPlayerRepository(track: Track): PlayerRepository {
        return PlayerRepositoryImpl(Player(track.mapToTrackDto()))
    }

    fun providePlayerInteractor(track: Track): PlayerInteractor {
        return PlayerInteractorImpl(getPlayerRepository(track))
    }

    private fun getSettingsRepository(context: Context): SettingsRepository {
        return SettingsRepositoryImpl (
            ThemeStorage(context.getSharedPreferences(Constants.PLAYLIST_MAKER_PREFS, Context.MODE_PRIVATE))
        )
    }

    fun provideThemeSwitchInteractor(context: Context): SettingsInteractor {
        return SettingsInteractorImpl(getSettingsRepository(context))
    }
}