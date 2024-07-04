package com.practicum.playlistmaker.player.ui

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.Constants
import com.practicum.playlistmaker.player.domain.PlayerInteractor
import com.practicum.playlistmaker.search.domain.Track
import com.practicum.playlistmaker.search.domain.TracksInteractor
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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
    private var playTimer: Job?=null

    fun preparePlayer(url: String?) {
        renderState(PlayerScreenState.Preparing)
        if(url!=null){
            playerInteractor.preparePlayer(url=url, onPreparedListener = {
               renderState(PlayerScreenState.Stopped)
            },
                onCompletionListener={
                    playTimer?.cancel()
                    renderState(PlayerScreenState.Stopped)
                })
        }else{
            renderState(PlayerScreenState.Unplayable)
        }
    }


    private fun startPlayer() {
        playerInteractor.startPlayer()
        renderState(PlayerScreenState.Playing)
        updatePlayingTime()
    }

    fun pausePlayer() {
        playerInteractor.pausePlayer()
        renderState(PlayerScreenState.Paused)
        playTimer?.cancel()
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
       playTimer = viewModelScope.launch {
            while (isPlaying()) {
                delay(Constants.REFRESH_TIMER_DELAY)
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
            }
        }
    }


    fun getTrack(): Track {
        return trackInteractor
            .getHistory()
            .first()
    }
}