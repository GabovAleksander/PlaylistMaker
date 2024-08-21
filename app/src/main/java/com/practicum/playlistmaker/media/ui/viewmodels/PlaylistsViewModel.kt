package com.practicum.playlistmaker.media.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.media.domain.PlaylistsInteractor
import com.practicum.playlistmaker.new_playlist.domain.models.Playlist
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PlaylistsViewModel(private val interactor: PlaylistsInteractor) :
    ViewModel() {

    private val stateLiveData = MutableLiveData<PlaylistsScreenState>()
    fun observeState(): LiveData<PlaylistsScreenState> = stateLiveData

    private fun renderState(state: PlaylistsScreenState) {
        stateLiveData.postValue(state)
    }

    fun updatePlaylists() {
        viewModelScope.launch {
            val playlists = interactor.getPlaylists()
            if (playlists.isEmpty()) {
                renderState(PlaylistsScreenState.Empty)
            } else {
                renderState(PlaylistsScreenState.Content(playlists))
            }
        }
    }
}