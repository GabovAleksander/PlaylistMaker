package com.practicum.playlistmaker.media.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.media.domain.PlaylistsInteractor
import com.practicum.playlistmaker.new_playlist.domain.models.Playlist
import com.practicum.playlistmaker.search.domain.Track
import com.practicum.playlistmaker.utils.debounce
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BottomSheetViewModel(
    private val interactor: PlaylistsInteractor,
) : ViewModel() {

    private val playlistsStateLiveData = MutableLiveData<PlaylistsState>()

    fun observePlaylistsState(): LiveData<PlaylistsState> = playlistsStateLiveData

    var isClickable = true

    private val playlistClickDebounce =
        debounce<Boolean>(CLICK_DEBOUNCE_DELAY_MILLIS, viewModelScope, false) {
            isClickable = it
        }

    fun onPlaylistClicked() {
        isClickable = false
        playlistClickDebounce(true)
    }

    fun fillData() {
        viewModelScope.launch {
            val playlists = interactor.getPlaylists()
            if (playlists.isEmpty()) {
                playlistsStateLiveData.postValue(PlaylistsState.Empty)
            } else {
                playlistsStateLiveData.postValue(PlaylistsState.Playlists(playlists))
            }
        }
    }

    fun processResult(track: Track, playlist: Playlist) {
        viewModelScope.launch {
            if (interactor.isTrackAlreadyExists(track.trackId, playlist.playlistId)) {
                playlistsStateLiveData.postValue(
                    PlaylistsState.AddTrackResult(
                        false,
                        playlistName = playlist.name
                    )
                )
            } else {
                interactor.addTrack(track, playlist.playlistId)
                playlistsStateLiveData.postValue(
                    PlaylistsState.AddTrackResult(
                        true,
                        playlistName = playlist.name
                    )
                )
            }
        }
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY_MILLIS = 1000L
    }
}