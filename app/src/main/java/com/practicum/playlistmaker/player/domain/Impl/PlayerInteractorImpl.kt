package com.practicum.playlistmaker.player.domain.Impl


import com.practicum.playlistmaker.player.domain.PlayerInteractor
import com.practicum.playlistmaker.player.domain.PlayerRepository

class PlayerInteractorImpl(private val playerRepository: PlayerRepository): PlayerInteractor {

    override fun preparePlayer(prepare: () -> Unit) {
        playerRepository.preparePlayer(prepare)
    }

    override fun startPlayer() {
        playerRepository.startPlayer()
    }

    override fun pausePlayer() {
        playerRepository.pausePlayer()
    }

    override fun release() {
        playerRepository.release()
    }

    override fun setOnCompleteListener(onComplete: () -> Unit) {
        playerRepository.setOnCompletionListener(onComplete)
    }

    override fun getPosition(): Int {
        return playerRepository.getPosition()
    }

}