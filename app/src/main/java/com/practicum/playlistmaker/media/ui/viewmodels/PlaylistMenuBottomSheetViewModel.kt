package com.practicum.playlistmaker.media.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.media.domain.PlaylistsInteractor
import com.practicum.playlistmaker.new_playlist.domain.models.Playlist
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlaylistMenuBottomSheetViewModel(
    private val playListsInteractor: PlaylistsInteractor
): ViewModel() {

    var isClickable = true

    fun clickDebounce(): Boolean {
        val current = isClickable
        if (isClickable) {
            isClickable = false
            viewModelScope.launch {
                delay(CLICK_DEBOUNCE_DELAY_MILLIS)
                isClickable = true
            }
        }
        return current
    }

    fun deletePlaylist(playlist: Playlist, onResultListener: () -> Unit) {
        viewModelScope.launch {
            playListsInteractor.deletePlaylist(playlist)
            onResultListener()
        }
    }
companion object{
    private const val CLICK_DEBOUNCE_DELAY_MILLIS=2000L
}

}