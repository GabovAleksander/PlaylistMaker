package com.practicum.playlistmaker.player.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.Constants
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.search.domain.Impl.TrackGetterImpl
import com.practicum.playlistmaker.databinding.ActivityPlayerBinding
import com.practicum.playlistmaker.player.domain.TrackDto
import com.practicum.playlistmaker.search.domain.Track
import com.practicum.playlistmaker.search.model.mapToTrackDto
import com.practicum.playlistmaker.search.ui.TRACK_KEY

class PlayerActivity : AppCompatActivity() {
    private lateinit var viewModel: PlayerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        @Suppress("DEPRECATION") val track =
            intent.getSerializableExtra(Constants.TRACK) as Track

        viewModel = ViewModelProvider(
            this,
            PlayerViewModel.getViewModelFactory(track = track)
        )[PlayerViewModel::class.java]


        binding.buttonBack.setOnClickListener {
            finish()
        }

        binding.buttonPlay.apply {
            setOnClickListener {
                viewModel.playbackControl()
            }
        }

        viewModel.screenState.observe(this) {
            it.render(binding)
        }

        binding.buttonLike.apply {
            setOnClickListener {
                if (viewModel.switchLike())
                    binding.buttonLike.background = AppCompatResources.getDrawable(binding.root.context,R.drawable.button_heart)
                else binding.buttonLike.background =AppCompatResources.getDrawable(binding.root.context,R.drawable.button_heart_inactive)
            }
        }

            }


    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.release()
    }
}