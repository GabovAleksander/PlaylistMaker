package com.practicum.playlistmaker.player.data

interface PlayerClient {
    fun preparePlayer(prepare: () -> Unit)
    fun startPlayer()
    fun pausePlayer()
    fun setOnCompletionListener(onComplete: () -> Unit)

    fun release()

    fun getCurrentTime(): Int

}