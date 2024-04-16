package com.practicum.playlistmaker.presentation

import android.media.MediaPlayer
import com.practicum.playlistmaker.domain.PlayerInteractor


class PlayerInteractorImpl : PlayerInteractor {
    companion object {
        const val STATE_DEFAULT = 0
        const val STATE_PREPARED = 1
        const val STATE_PLAYING = 2
        const val STATE_PAUSED = 3
        const val UPDATE_DELAY = 300L
    }

    private val mediaPlayer=MediaPlayer()
    override var playerState = STATE_DEFAULT
    override var url: String? = ""


    override fun release() {
        mediaPlayer.release()
    }

    override fun preparePlayer(callback: () -> Unit) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playerState = STATE_PREPARED
            callback()
        }
    }

    override fun startPlayer(callback: () -> Unit) {
        mediaPlayer.start()
        playerState = STATE_PLAYING
        callback()
    }

    override fun pausePlayer(callback: () -> Unit) {
        mediaPlayer.pause()
        playerState = STATE_PAUSED
        callback()
    }

    override fun playbackControl(callbackWhenPlay: () -> Unit,callbackWhenPause:()->Unit) {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer{callbackWhenPlay()}
            }

            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer{callbackWhenPause()}
            }
        }
    }
    override fun getCurrentPosition(): Int {
    return mediaPlayer.currentPosition
    }
}