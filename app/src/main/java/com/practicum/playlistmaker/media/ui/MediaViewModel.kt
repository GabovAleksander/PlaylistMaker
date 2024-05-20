package com.practicum.playlistmaker.media.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.main.ui.ScreenViewState
import com.practicum.playlistmaker.search.ui.SingleLiveEvent

class MediaViewModel() : ViewModel() {
    private val screenViewStateLiveData = SingleLiveEvent<ScreenViewState>()

    fun observeNavigationViewState(): LiveData<ScreenViewState> = screenViewStateLiveData
}