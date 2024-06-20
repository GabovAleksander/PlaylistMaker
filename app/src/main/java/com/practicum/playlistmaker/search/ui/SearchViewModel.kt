package com.practicum.playlistmaker.search.ui

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.Constants
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.main.ui.ScreenViewState
import com.practicum.playlistmaker.search.domain.Track
import com.practicum.playlistmaker.search.domain.TracksInteractor

class SearchViewModel(private val tracksInteractor: TracksInteractor) : ViewModel() {


    private val _screenState = MutableLiveData<SearchState>()
    fun observeState():LiveData<SearchState> = _screenState
    private val handler = Handler(Looper.getMainLooper())
    private var lastQuery: String? = null
    private val _trackIsClickable = MutableLiveData(true)
    var trackIsClickable: LiveData<Boolean> = _trackIsClickable

    init {
        val history=showHistory()
        if(history.isNotEmpty()){
            renderState(SearchState.ShowHistory(history))
        }
    }

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


    fun getTracks(query: String) {
            if (query.isNotEmpty()) {
                handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
                renderState(SearchState.Loading)
                tracksInteractor.searchTracks(query, object : TracksInteractor.TracksConsumer {
                    override fun consume(foundTracks: ArrayList<Track>?, errorMessage: String?) {

                        val tracks = arrayListOf<Track>()
                        if (foundTracks != null) {
                            tracks.addAll(foundTracks)
                        }

                        when {
                            errorMessage != null -> {
                                renderState(
                                    SearchState.Error(
                                        message = errorMessage
                                    )
                                )
                            }

                            tracks.isEmpty() -> {
                                renderState(
                                    SearchState.NothingFound
                                )
                            }
                        else -> {
                                renderState(
                                    SearchState.Success(tracks=tracks
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

    override fun onCleared() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }

    companion object {
        private val SEARCH_REQUEST_TOKEN = Any()
    }
}