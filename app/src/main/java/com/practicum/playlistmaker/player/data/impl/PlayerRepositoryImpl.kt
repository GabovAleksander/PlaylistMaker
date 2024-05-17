package com.practicum.playlistmaker.player.data.impl

import com.practicum.playlistmaker.player.data.PlayerClient
import com.practicum.playlistmaker.player.domain.PlayerRepository

class PlayerRepositoryImpl(private val playerClient: PlayerClient): PlayerRepository {
    override fun preparePlayer(prepare: () -> Unit) {
        playerClient.preparePlayer(prepare)
    }

    override fun setOnCompletionListener(onComplete: () -> Unit) {
        playerClient.setOnCompletionListener(onComplete)
    }

    override fun startPlayer() {
        playerClient.startPlayer()
    }

    override fun pausePlayer() {
        playerClient.pausePlayer()
    }

    override fun release() {
        playerClient.release()
    }

    override fun getPosition(): Int {
        return playerClient.getCurrentTime()
    }

}