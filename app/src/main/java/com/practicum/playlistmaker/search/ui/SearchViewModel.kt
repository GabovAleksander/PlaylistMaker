package com.practicum.playlistmaker.search.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.search.domain.ResponseDataStatus
import com.practicum.playlistmaker.search.domain.Track
import com.practicum.playlistmaker.search.domain.TracksInteractor
import com.practicum.playlistmaker.utils.debounce
import kotlinx.coroutines.launch

class SearchViewModel(private val tracksInteractor: TracksInteractor) : ViewModel() {


    private val _screenState = MutableLiveData<SearchState>()
    fun observeState(): LiveData<SearchState> = _screenState

    var isClickable = true

    init {
        val history = showHistory()
        if (history.isNotEmpty()) {
            renderState(SearchState.ShowHistory(history))
        }
    }

    private val tracksSearchDebounce =
        debounce<String>(SEARCH_DEBOUNCE_DELAY, viewModelScope, true) { query ->
            getTracks(query)
        }

    private val trackClickDebounce =
        debounce<Boolean>(CLICK_DEBOUNCE_DELAY, viewModelScope, false) {
            isClickable = it
        }

    fun searchDebounce(query: String) {
        if (query.isNotEmpty()) {
            tracksSearchDebounce(query)
        }
    }

    fun onTrackClick() {
        isClickable = false
        trackClickDebounce(true)
    }

    fun getTracks(query: String) {
        if (query.isNotEmpty()) {
            renderState(SearchState.Loading)
            viewModelScope.launch {
                tracksInteractor.searchTracks(query)
                    .collect { data -> processResult(data) }
            }
        }
    }

    private fun processResult(result: ResponseDataStatus) {

        when {
            result.status != null -> {
                renderState(SearchState.Error(message = result.status))
            }

            result.tracks.isNullOrEmpty() -> {
                renderState(SearchState.NothingFound)
            }

            else -> {
                renderState(SearchState.Success(tracks = result.tracks))
            }
        }
    }

    fun addToHistory(track: Track) {
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
companion object{
    private const val CLICK_DEBOUNCE_DELAY = 1000L
    private const val SEARCH_DEBOUNCE_DELAY = 2000L
}
}