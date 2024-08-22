package com.practicum.playlistmaker.new_playlist.ui.viewmodels

import android.Manifest
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.media.domain.PlaylistsInteractor
import com.practicum.playlistmaker.utils.debounce
import kotlinx.coroutines.launch

class NewPlaylistViewModel(
    private val interactor: PlaylistsInteractor,
) : ViewModel() {

    var isClickable = true
    private val clickDebounce =
        debounce<Boolean>(CLICK_DEBOUNCE_DELAY_MILLIS, viewModelScope, false) {
            isClickable = it
        }

    fun onBtnClick() {
        isClickable = false
        clickDebounce(true)
    }


    fun createPlaylist(
        name: String,
        description: String,
        imageUri: Uri?,
        onResultListener: () -> Unit
    ) {
        viewModelScope.launch {
            interactor.createPlaylist(name, description, imageUri)
            onResultListener()
        }
    }

    fun updatePlaylist(
        playListId: Int,
        name: String,
        description: String,
        imageUri: Uri?,
        onResultListener: () -> Unit
    ) {
        viewModelScope.launch {
            interactor.updatePlaylist(playListId, name, description, imageUri)
            onResultListener()
        }
    }
    companion object{
        private const val CLICK_DEBOUNCE_DELAY_MILLIS=1000L
    }
}