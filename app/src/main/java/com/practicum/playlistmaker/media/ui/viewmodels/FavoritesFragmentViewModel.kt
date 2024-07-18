package com.practicum.playlistmaker.media.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.media.domain.FavoritesInteractor
import com.practicum.playlistmaker.search.domain.Track
import com.practicum.playlistmaker.search.domain.TracksInteractor
import com.practicum.playlistmaker.utils.debounce
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoritesFragmentViewModel(
    private val tracksInteractor: TracksInteractor,
    private val interactor: FavoritesInteractor
) : ViewModel() {

    private val contentStateLiveData = MutableLiveData<FavoritesState>()
    fun observeContentState(): LiveData<FavoritesState> = contentStateLiveData

    var isClickable = true

    private val trackClickDebounce =
        debounce<Boolean>(CLICK_DEBOUNCE_DELAY, viewModelScope, false) {
            isClickable = it
        }

    init {
        getFavoritesTracks()
    }

    fun getFavoritesTracks() {
        viewModelScope.launch(Dispatchers.IO) {
            interactor
                .getFavoritesTracks()
                .collect { favoritesTracks ->
                    launch (Dispatchers.Main){processResult(favoritesTracks)}
                }
        }
    }

    private fun processResult(trackList: List<Track>) {
        when {
            trackList.isEmpty() -> {
                contentStateLiveData.value = FavoritesState.Empty
            }
            else -> {
                contentStateLiveData.value = FavoritesState.FavoritesTracks(trackList)
            }
        }
    }
    fun addToHistory(track: Track) {
        tracksInteractor.addTrackToHistory(track)

    }
    fun onTrackClick() {
        isClickable = false
        trackClickDebounce(true)
    }
    companion object{
        private const val CLICK_DEBOUNCE_DELAY = 300L
    }
}
