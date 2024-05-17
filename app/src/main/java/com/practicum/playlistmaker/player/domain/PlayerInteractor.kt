package com.practicum.playlistmaker.player.domain

interface PlayerInteractor {
    fun preparePlayer(prepare: () -> Unit)
    fun startPlayer()
    fun pausePlayer()
    fun release()
    fun setOnCompleteListener(onComplete: ()->Unit)
    fun getPosition(): Int

}