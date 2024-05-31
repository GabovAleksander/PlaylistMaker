package com.practicum.playlistmaker.player.ui

sealed interface PlayerScreenState {
    object Preparing : PlayerScreenState

    object Stopped : PlayerScreenState
    object Paused : PlayerScreenState

    object Playing : PlayerScreenState

    object Unplayable : PlayerScreenState

    data class TimerUpdating(val time: String) : PlayerScreenState

}