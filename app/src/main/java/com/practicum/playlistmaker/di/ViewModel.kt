package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.main.ui.MainViewModel
import com.practicum.playlistmaker.player.ui.PlayerViewModel
import com.practicum.playlistmaker.search.ui.SearchViewModel
import com.practicum.playlistmaker.settings.ui.SettingsViewModel
import com.practicum.playlistmaker.media.ui.MediaViewModel
import com.practicum.playlistmaker.media.ui.viewmodels.FavoritesViewModel
import com.practicum.playlistmaker.media.ui.viewmodels.PlaylistsViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val viewModelModule = module {


    viewModelOf(::MainViewModel).bind()

    viewModelOf(::SearchViewModel).bind()

    viewModelOf(::PlayerViewModel).bind()

    viewModelOf(::SettingsViewModel).bind()

    viewModelOf(::MediaViewModel).bind()

    viewModelOf(::FavoritesViewModel).bind()

    viewModelOf(::PlaylistsViewModel).bind()

}