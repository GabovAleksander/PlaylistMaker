package com.practicum.playlistmaker.search.ui

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.practicum.playlistmaker.Constants
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.search.domain.Track
import com.practicum.playlistmaker.search.domain.TracksInteractor

class SearchViewModel(application: Application) : AndroidViewModel(application) {

    private val tracksInteractor = Creator.provideTracksInteractor(getApplication())
    private val _screenState = MutableLiveData<SearchState>()
    private val showToast = SingleLiveEvent<String>()
    private val handler = Handler(Looper.getMainLooper())
    private var lastQuery: String? = null
    private val _trackIsClickable = MutableLiveData(true)
    var trackIsClickable: LiveData<Boolean> = _trackIsClickable

    init {
        _screenState.postValue(SearchState.ShowHistory(tracks = showHistory()))
    }

    fun observeState(): LiveData<SearchState> = _screenState

    fun observeShowToast(): LiveData<String> = showToast

    private fun makeDelaySearching(changedText: String) {
        val searchRunnable = Runnable { getTracks(changedText) }
        val postTime = SystemClock.uptimeMillis() + Constants.SEARCH_DEBOUNCE_DELAY
        handler.postAtTime(
            searchRunnable,
            SEARCH_REQUEST_TOKEN,
            postTime,
        )
    }

    fun searchDebounce(changedText: String? = lastQuery) {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
        if (!changedText.isNullOrEmpty()) {
            if ((lastQuery == changedText)) {
                return
            }
            this.lastQuery = changedText
            makeDelaySearching(changedText)
        }
    }


    fun onSearchClicked(track: Track) {
        trackOnClickDebounce()
        addToHistory(track)
    }

    private fun trackOnClickDebounce() {
        _trackIsClickable.value = false
        handler.postDelayed({ _trackIsClickable.value = true }, Constants.CLICK_DEBOUNCE_DELAY)
    }

    fun onDestroy() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }


    fun getTracks(query: String? = lastQuery) {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
        query?.let {
            _screenState.postValue(SearchState.Loading)
            tracksInteractor.searchTracks(query, object : TracksInteractor.TracksConsumer {
                override fun consume(
                    foundTracks: List<Track>?,
                    errorMessage: String?,
                    code: Int
                ) {
                    when (code) {
                        Constants.SUCCESS_CODE -> {
                            val tracks = arrayListOf<Track>()
                            if (foundTracks!!.isNotEmpty()) {
                                tracks.addAll(foundTracks)

                                _screenState.postValue(SearchState.Success(tracks = tracks))
                            } else {
                                _screenState.postValue(SearchState.NothingFound)
                            }
                        }
                        else -> {
                            _screenState.postValue(
                                SearchState.Error(
                                    message = getApplication<Application>().getString(R.string.something_went_wrong),
                                )
                            )
                        }
                    }
                }
            })
        }
    }

    private fun addToHistory(track: Track) {
        tracksInteractor.addTrackToHistory(track)

    }

    fun clearSearch() {
        val historyTracks = showHistory()
        if (historyTracks.isNotEmpty()) {
            renderState(SearchState.ShowHistory(historyTracks))
        } else {
            renderState(SearchState.Success(arrayListOf()))
        }
    }

    fun clearHistory() {
        tracksInteractor.clearHistory()
        _screenState.postValue(SearchState.Success(arrayListOf()))

    }

    private fun showHistory(): ArrayList<Track> {
        return tracksInteractor.getHistory()
    }

    private fun renderState(state: SearchState) {
        _screenState.postValue(state)
    }



    companion object {
        private val SEARCH_REQUEST_TOKEN = Any()
    }
}