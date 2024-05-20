package com.practicum.playlistmaker.player.domain

import android.media.MediaPlayer.OnCompletionListener
import android.media.MediaPlayer.OnPreparedListener

interface PlayerRepository {

    fun preparePlayer(url: String, onPreparedListener: () -> Unit, onCompletionListener: () -> Unit)

    fun startPlayer()

    fun pausePlayer()

    fun isPlaying(): Boolean

    fun getPosition(): Int

}