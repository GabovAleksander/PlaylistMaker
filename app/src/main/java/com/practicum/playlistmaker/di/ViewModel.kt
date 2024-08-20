package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.player.ui.PlayerViewModel
import com.practicum.playlistmaker.search.ui.SearchViewModel
import com.practicum.playlistmaker.settings.ui.SettingsViewModel
import com.practicum.playlistmaker.media.ui.MediaViewModel
import com.practicum.playlistmaker.media.ui.viewmodels.BottomSheetViewModel
import com.practicum.playlistmaker.media.ui.viewmodels.FavoritesFragmentViewModel
import com.practicum.playlistmaker.media.ui.viewmodels.PlaylistsViewModel
import com.practicum.playlistmaker.new_playlist.ui.viewmodels.NewPlaylistViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val viewModelModule = module {


    viewModelOf(::SearchViewModel).bind()

    viewModelOf(::PlayerViewModel).bind()

    viewModelOf(::SettingsViewModel).bind()

    viewModelOf(::MediaViewModel).bind()

    viewModelOf(::FavoritesFragmentViewModel).bind()

    viewModelOf(::PlaylistsViewModel).bind()

    viewModelOf(::NewPlaylistViewModel).bind()

    viewModelOf(::BottomSheetViewModel).bind()

}