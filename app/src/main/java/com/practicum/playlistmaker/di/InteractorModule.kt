package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.player.domain.Impl.PlayerInteractorImpl
import com.practicum.playlistmaker.player.domain.PlayerInteractor
import com.practicum.playlistmaker.media.domain.impl.FavoritesInteractorImpl
import com.practicum.playlistmaker.media.domain.FavoritesInteractor
import com.practicum.playlistmaker.search.domain.Impl.TracksInteractorImpl
import com.practicum.playlistmaker.search.domain.TracksInteractor
import com.practicum.playlistmaker.settings.domain.Impl.SettingsInteractorImpl
import com.practicum.playlistmaker.settings.domain.SettingsInteractor
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val interactorModule = module {

    singleOf(::TracksInteractorImpl).bind<TracksInteractor>()

    singleOf(::PlayerInteractorImpl).bind<PlayerInteractor>()

    singleOf(::SettingsInteractorImpl).bind<SettingsInteractor>()

    factoryOf(::FavoritesInteractorImpl).bind<FavoritesInteractor>()

}