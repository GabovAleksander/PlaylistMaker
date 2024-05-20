package com.practicum.playlistmaker.main.ui


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.practicum.playlistmaker.databinding.ActivityMainBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModel()
    private val router = NavigationRouter(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainViewModel.observeNavigationViewState().observe(this) {
            screen(it)
        }
        initButtonMedia()
        initButtonSearch()
        initButtonSettings()
    }

    private fun screen(state: ScreenViewState) {
        when (state) {
            is ScreenViewState.Search -> router.toSearch()
            is ScreenViewState.Settings -> router.toSettings()
            is ScreenViewState.Media -> router.toLibrary()
        }
    }

    private fun initButtonSearch() {
        binding.buttonSearch.setOnClickListener() {
            mainViewModel.searchView()
        }
    }

    private fun initButtonMedia() {
        binding.buttonMedia.setOnClickListener() {
            mainViewModel.mediaView()
        }
    }

    private fun initButtonSettings() {
        binding.buttonSettings.setOnClickListener() {
            mainViewModel.settingsView()
        }
    }
}