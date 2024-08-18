package com.practicum.playlistmaker.player.data

import android.media.MediaPlayer

class Player(private val client: MediaPlayer) : PlayerClient {


    override fun preparePlayer(
        url: String,
        onPreparedListener: () -> Unit,
        onCompletionListener: () -> Unit
    ) {
        client.reset()
        client.setDataSource(url)
        client.prepareAsync()


        client.setOnPreparedListener {
            onPreparedListener.invoke()
        }
        client.setOnCompletionListener {
            onCompletionListener.invoke()
        }
    }

    override fun startPlayer() {
        client.start()
    }

    override fun pausePlayer() {
        client.pause()
    }

    override fun isPlaying(): Boolean {
        return client.isPlaying
    }

    override fun getCurrentTime(): Int {
        return client.currentPosition
    }

}