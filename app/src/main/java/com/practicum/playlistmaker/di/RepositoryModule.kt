package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.media.domain.FavoritesRepository
import com.practicum.playlistmaker.media.data.impl.FavoritesRepositoryImpl
import com.practicum.playlistmaker.player.data.impl.PlayerRepositoryImpl
import com.practicum.playlistmaker.player.domain.PlayerRepository
import com.practicum.playlistmaker.search.data.impl.TracksRepositoryImpl
import com.practicum.playlistmaker.search.domain.TracksRepository
import com.practicum.playlistmaker.settings.data.Impl.SettingsRepositoryImpl
import com.practicum.playlistmaker.settings.domain.SettingsRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val repositoryModule = module {


    singleOf(::TracksRepositoryImpl).bind<TracksRepository>()

    factory<PlayerRepository> {
        PlayerRepositoryImpl(playerClient = get())
    }

    singleOf(::SettingsRepositoryImpl).bind<SettingsRepository>()

    singleOf(::FavoritesRepositoryImpl).bind<FavoritesRepository>()
}