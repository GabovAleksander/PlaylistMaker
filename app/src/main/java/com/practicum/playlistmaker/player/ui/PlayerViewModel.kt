package com.practicum.playlistmaker.player.ui

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.Constants
import com.practicum.playlistmaker.player.domain.PlayerInteractor
import com.practicum.playlistmaker.search.domain.Track
import com.practicum.playlistmaker.search.domain.TracksInteractor
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(
    private val playerInteractor: PlayerInteractor,
    private val trackInteractor: TracksInteractor
) : ViewModel() {

    private var likeState=false
    private var checkState=false

    private val stateLiveData = MutableLiveData<PlayerScreenState>()

    fun observeState(): LiveData<PlayerScreenState> =stateLiveData
    private val handler =Handler(Looper.getMainLooper())
    private val updatePlayingTimeRunnable= Runnable { updatePlayingTime() }
    fun preparePlayer(url: String?) {
        renderState(PlayerScreenState.Preparing)
        if(url!=null){
            playerInteractor.preparePlayer(url=url, onPreparedListener = {
               renderState(PlayerScreenState.Stopped)
            },
                onCompletionListener={
                    handler.removeCallbacks(updatePlayingTimeRunnable)
                    renderState(PlayerScreenState.Playing)
                })
        }else{
            renderState(PlayerScreenState.Unplayable)
        }
    }


    private fun startPlayer() {
        playerInteractor.startPlayer()
        renderState(PlayerScreenState.Playing)
        handler.postDelayed(updatePlayingTimeRunnable, Constants.REFRESH_TIMER_DELAY)
    }

    fun pausePlayer() {
        playerInteractor.pausePlayer()
        renderState(PlayerScreenState.Paused)
        handler.removeCallbacks(updatePlayingTimeRunnable)
    }

    fun isPlaying():Boolean{
        return playerInteractor.isPlaying()
    }


    private fun getCurrentPosition(): Int {
        return playerInteractor.getPosition()
    }


    fun playbackControl() {
        if(isPlaying()){
            pausePlayer()
        }else{
            startPlayer()
        }
    }
    private fun renderState(state: PlayerScreenState) {
        stateLiveData.postValue(state)
    }
    fun switchLike():Boolean{
        likeState=!likeState
        return likeState
    }

    fun switchCheck():Boolean{
        checkState=!checkState
        return checkState
    }

    private fun updatePlayingTime() {
        renderState(
            PlayerScreenState.TimerUpdating(
                SimpleDateFormat(
                    "mm:ss",
                    Locale.getDefault()
                ).format(
                    getCurrentPosition()
                )
            )
        )
        handler.postDelayed(updatePlayingTimeRunnable, Constants.REFRESH_TIMER_DELAY)
    }

    override fun onCleared() {
        pausePlayer()
        handler.removeCallbacksAndMessages(null)
    }

    fun getTrack(): Track {
        return trackInteractor
            .getHistory()
            .first()
    }
}