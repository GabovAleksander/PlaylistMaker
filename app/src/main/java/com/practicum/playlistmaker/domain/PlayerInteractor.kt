package com.practicum.playlistmaker.domain

interface PlayerInteractor {

    enum class Status(val value:Int){
        STATE_DEFAULT(0),
        STATE_PREPARED(1),
        STATE_PLAYING(2),
        STATE_PAUSED(3),
    }
    var playerState: Status

    var url: String?
    fun preparePlayer(callback: () -> Unit)
    fun startPlayer(callback: ()->Unit)
    fun pausePlayer(callback: () -> Unit)
    fun playbackControl(callbackWhenPlay: () -> Unit, callbackWhenPause: () -> Unit)
    fun release()
    fun getCurrentPosition():Int

}