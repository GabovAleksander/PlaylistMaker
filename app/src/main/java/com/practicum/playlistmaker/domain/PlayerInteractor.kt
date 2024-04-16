package com.practicum.playlistmaker.domain

interface PlayerInteractor {

    var playerState: Int

    var url: String?
    fun preparePlayer(callback: () -> Unit)
    fun startPlayer(callback: ()->Unit)
    fun pausePlayer(callback: () -> Unit)
    fun playbackControl(callbackWhenPlay: () -> Unit, callbackWhenPause: () -> Unit)
    fun release()
    fun getCurrentPosition():Int

}