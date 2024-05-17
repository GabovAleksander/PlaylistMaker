package com.practicum.playlistmaker.player.domain

interface PlayerRepository {

        fun preparePlayer(prepare: () -> Unit)

        fun setOnCompletionListener(onComplete: () -> Unit)

        fun startPlayer()

        fun pausePlayer()

        fun release()

        fun getPosition(): Int

}