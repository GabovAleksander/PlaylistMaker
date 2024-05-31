package com.practicum.playlistmaker.player.data.impl

import android.media.MediaPlayer.OnCompletionListener
import android.media.MediaPlayer.OnPreparedListener
import com.practicum.playlistmaker.player.data.PlayerClient
import com.practicum.playlistmaker.player.domain.PlayerRepository

class PlayerRepositoryImpl(private val playerClient: PlayerClient) : PlayerRepository {
    override fun preparePlayer(
        url: String,
        onPreparedListener: () -> Unit,
        onCompletionListener: () -> Unit
    ) {
        playerClient.preparePlayer(url, onPreparedListener, onCompletionListener)
    }

    override fun startPlayer() {
        playerClient.startPlayer()
    }

    override fun pausePlayer() {
        playerClient.pausePlayer()
    }

    override fun isPlaying(): Boolean {
        return playerClient.isPlaying()
    }

    override fun getPosition(): Int {
        return playerClient.getCurrentTime()
    }

}