package com.practicum.playlistmaker.player.ui


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.media.domain.FavoritesInteractor
import com.practicum.playlistmaker.player.domain.PlayerInteractor
import com.practicum.playlistmaker.search.domain.Track
import com.practicum.playlistmaker.search.domain.TracksInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(
    private val playerInteractor: PlayerInteractor,
    private val trackInteractor: TracksInteractor,
    private val favoritesInteractor: FavoritesInteractor
) : ViewModel() {
    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }
    private var isLike = false
    private var checkState = false
    private val stateLiveData = MutableLiveData<PlayerScreenState>()
    private val isLikeLiveData = MutableLiveData<Boolean>()
    fun observeLikeState(): LiveData<Boolean> = isLikeLiveData
    fun observeState(): LiveData<PlayerScreenState> = stateLiveData
    private var playTimer: Job? = null

    fun preparePlayer(url: String?) {
        renderState(PlayerScreenState.Preparing)
        if (url != null) {
            playerInteractor.preparePlayer(url = url, onPreparedListener = {
                renderState(PlayerScreenState.Stopped)
            },
                onCompletionListener = {
                    playTimer?.cancel()
                    renderState(PlayerScreenState.Stopped)
                })
        } else {
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

    fun isPlaying(): Boolean {
        return playerInteractor.isPlaying()
    }


    private fun getCurrentPosition(): Int {
        return playerInteractor.getPosition()
    }


    fun playbackControl() {
        if (isPlaying()) {
            pausePlayer()
        } else {
            startPlayer()
        }
    }

    private fun renderState(state: PlayerScreenState) {
        stateLiveData.postValue(state)
    }

    fun switchLike(track: Track): Boolean {
        isLike = !isLike
        isLikeLiveData.value = isLike
        viewModelScope.launch(Dispatchers.IO) {
            if (isLike) {
                favoritesInteractor.addTrack(track)
            } else {
                favoritesInteractor.deleteTrack(track.trackId)
            }
        }
        return isLike
    }

    fun isLike(trackId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            favoritesInteractor.isFavorite(trackId).collect {
                isLike = it
                isLikeLiveData.postValue(isLike)
            }
        }
    }

    fun switchCheck(): Boolean {
        checkState = !checkState
        return checkState
    }


    private fun updatePlayingTime() {
        playTimer?.cancel()
        playTimer = viewModelScope.launch {
            while (isPlaying()) {
                delay(REFRESH_TIMER_DELAY)
                renderState(
                    PlayerScreenState.TimerUpdating(
                        dateFormat.format(
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

    companion object {
        private const val REFRESH_TIMER_DELAY = 300L
    }
}