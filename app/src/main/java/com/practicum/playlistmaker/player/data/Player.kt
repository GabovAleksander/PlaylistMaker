package com.practicum.playlistmaker.player.data

import android.media.MediaPlayer
import com.practicum.playlistmaker.player.domain.TrackDto

class Player(track: TrackDto):PlayerClient {
    private val mediaPlayer: MediaPlayer = MediaPlayer()
    init {
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
    }

    override fun preparePlayer(prepare: () -> Unit) {
        mediaPlayer.setOnPreparedListener { prepare() }
    }

    override fun setOnCompletionListener(onComplete: () -> Unit) {
        mediaPlayer.setOnCompletionListener {
            onComplete()
        }
    }

    override fun startPlayer() {
        mediaPlayer.start()
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
    }

    override fun release() {
        mediaPlayer.release()
    }

    override fun getCurrentTime(): Int {
        return mediaPlayer.currentPosition
    }

}