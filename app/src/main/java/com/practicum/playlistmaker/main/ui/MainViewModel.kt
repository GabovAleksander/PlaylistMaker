package com.practicum.playlistmaker.main.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.search.ui.SingleLiveEvent

class MainViewModel : ViewModel() {
    private val screenViewStateLiveData = SingleLiveEvent<ScreenViewState>()

    fun observeNavigationViewState(): LiveData<ScreenViewState> = screenViewStateLiveData

    fun searchView() {
        screenViewStateLiveData.postValue(ScreenViewState.Search)
    }

    fun mediaView() {
        screenViewStateLiveData.postValue(ScreenViewState.Media)
    }

    fun settingsView() {
        screenViewStateLiveData.postValue(ScreenViewState.Settings)
    }
}