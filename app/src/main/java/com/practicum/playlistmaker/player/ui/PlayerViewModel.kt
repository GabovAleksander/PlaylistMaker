package com.practicum.playlistmaker.player.ui

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.Constants
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.player.domain.TrackDto
import com.practicum.playlistmaker.search.domain.Track
import com.practicum.playlistmaker.search.model.mapToTrackDto
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(track: Track) : ViewModel() {

    private val _screenState = MutableLiveData<PlayerState>()
    val screenState: LiveData<PlayerState> = _screenState
    private val playerInteractor = Creator.providePlayerInteractor(track)
    private var playerState: PlayerStateCodes = PlayerStateCodes.STATE_DEFAULT
    private var likeState=false
    private var checkState=false

    private val handler: Handler = Handler(Looper.getMainLooper())
    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }
    private val timerGo =
        object : Runnable {
            override fun run() {
                updateTimer(getCurrentPosition())
                handler.postDelayed(
                    this,
                    Constants.REFRESH_TIMER_DELAY,
                )
            }
        }

    init {
        _screenState.value = PlayerState.BeginningState(track.mapToTrackDto())
        preparePlayer()
        setOnCompletionListener()
    }

    private fun preparePlayer() {
        playerInteractor.preparePlayer {
            playerState = PlayerStateCodes.STATE_PREPARED
            _screenState.value = PlayerState.Preparing()
        }
    }

    private fun setOnCompletionListener() {
        playerInteractor.setOnCompleteListener {
            playerState = PlayerStateCodes.STATE_PREPARED
            handler.removeCallbacks(timerGo)
            _screenState.value = PlayerState.PlayCompleting()
        }
    }

    private fun startPlayer() {
        playerInteractor.startPlayer()
        playerState = PlayerStateCodes.STATE_PLAYING
        handler.postDelayed(timerGo, Constants.REFRESH_TIMER_DELAY)
        _screenState.value = PlayerState.PlayButtonHandling(playerState)
    }

    fun pausePlayer() {
        playerInteractor.pausePlayer()
        playerState = PlayerStateCodes.STATE_PAUSED
        handler.removeCallbacks(timerGo)
        _screenState.value = PlayerState.PlayButtonHandling(playerState)
    }

    private fun updateTimer(time: String) {
        _screenState.postValue(PlayerState.TimerUpdating(time))
    }

    private fun getCurrentPosition(): String {
        return dateFormat.format(playerInteractor.getPosition())
    }

    fun release() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
        playerInteractor.release()
    }

    fun playbackControl() {
        when (playerState) {
            PlayerStateCodes.STATE_PLAYING -> {
                pausePlayer()
            }
            PlayerStateCodes.STATE_PREPARED, PlayerStateCodes.STATE_PAUSED -> {
                startPlayer()
            }
            PlayerStateCodes.STATE_DEFAULT -> {

            }
        }
    }

    fun switchLike():Boolean{
        likeState=!likeState
        return likeState
    }

    fun switchCheck():Boolean{
        checkState=!checkState
        return checkState
    }

    enum class PlayerStateCodes(val value:Int) {
        STATE_DEFAULT(0),
        STATE_PREPARED(1),
        STATE_PLAYING(2),
        STATE_PAUSED(3)
    }

    companion object {
        private val SEARCH_REQUEST_TOKEN = Any()

        fun getViewModelFactory(track: Track): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return PlayerViewModel(
                        track
                    ) as T
                }
            }
    }
}