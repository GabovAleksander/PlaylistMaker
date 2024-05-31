package com.practicum.playlistmaker.player.data

import android.media.MediaPlayer.OnPreparedListener

interface PlayerClient {
    fun preparePlayer(url: String, onPreparedListener: () -> Unit, onCompletionListener: () -> Unit)
    fun startPlayer()
    fun pausePlayer()
    fun isPlaying(): Boolean
    fun getCurrentTime(): Int

}