package com.practicum.playlistmaker.presentation

import android.media.MediaPlayer
import com.practicum.playlistmaker.domain.PlayerInteractor


class PlayerInteractorImpl : PlayerInteractor {

    private val mediaPlayer=MediaPlayer()
    override var playerState = PlayerInteractor.Status.STATE_DEFAULT
    override var url: String? = ""


    override fun release() {
        mediaPlayer.release()
    }

    override fun preparePlayer(callback: () -> Unit) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = PlayerInteractor.Status.STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playerState = PlayerInteractor.Status.STATE_PREPARED
            callback()
        }
    }

    override fun startPlayer(callback: () -> Unit) {
        mediaPlayer.start()
        playerState = PlayerInteractor.Status.STATE_PLAYING
        callback()
    }

    override fun pausePlayer(callback: () -> Unit) {
        mediaPlayer.pause()
        playerState = PlayerInteractor.Status.STATE_PAUSED
        callback()
    }

    override fun playbackControl(callbackWhenPlay: () -> Unit,callbackWhenPause:()->Unit) {
        when (playerState) {
            PlayerInteractor.Status.STATE_PLAYING -> {
                pausePlayer{callbackWhenPlay()}
            }

            PlayerInteractor.Status.STATE_PREPARED, PlayerInteractor.Status.STATE_PAUSED -> {
                startPlayer{callbackWhenPause()}
            }
            else->{

            }
        }
    }
    override fun getCurrentPosition(): Int {
    return mediaPlayer.currentPosition
    }
}