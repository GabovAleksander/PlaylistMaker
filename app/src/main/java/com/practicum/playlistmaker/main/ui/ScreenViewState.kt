package com.practicum.playlistmaker.main.ui

sealed interface ScreenViewState {
    object Search : ScreenViewState
    object Media : ScreenViewState
    object Settings : ScreenViewState
}