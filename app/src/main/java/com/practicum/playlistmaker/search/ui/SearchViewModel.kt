package com.practicum.playlistmaker.search.ui

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.Constants
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.main.ui.ScreenViewState
import com.practicum.playlistmaker.search.domain.ResponseDataStatus
import com.practicum.playlistmaker.search.domain.Track
import com.practicum.playlistmaker.search.domain.TracksInteractor
import com.practicum.playlistmaker.utils.debounce
import kotlinx.coroutines.launch

class SearchViewModel(private val tracksInteractor: TracksInteractor) : ViewModel() {


    private val _screenState = MutableLiveData<SearchState>()
    fun observeState():LiveData<SearchState> = _screenState

    var isClickable=true
    init {
        val history=showHistory()
        if(history.isNotEmpty()){
            renderState(SearchState.ShowHistory(history))
        }
    }

  private val tracksSearchDebounce= debounce<String>(Constants.SEARCH_DEBOUNCE_DELAY,viewModelScope, true){
      query -> getTracks(query)
  }

   private val trackClickDebounce =
       debounce<Boolean>(Constants.CLICK_DEBOUNCE_DELAY, viewModelScope,false){
           isClickable=it
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
               viewModelScope.launch {
                   tracksInteractor.searchTracks(query)
                       .collect{data -> processResult(data)}
               }
        }
    }

    private fun processResult(result:ResponseDataStatus) {
        val tracks = arrayListOf<Track>()
        if (result.tracks != null) {
            tracks.addAll(result.tracks)
        }

        when {
            result.status != null -> {
                renderState(SearchState.Error(message = result.status))
            }

            tracks.isEmpty() -> {
                renderState(SearchState.NothingFound)
            }

            else -> {
                renderState(SearchState.Success(tracks = tracks))
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

}