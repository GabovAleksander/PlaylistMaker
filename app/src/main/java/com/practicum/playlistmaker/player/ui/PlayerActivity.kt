package com.practicum.playlistmaker.player.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.Constants
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityPlayerBinding
import com.practicum.playlistmaker.search.domain.Track
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {
    val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }
    private lateinit var binding: ActivityPlayerBinding
    private val viewModel by viewModel<PlayerViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.observeState().observe(this) {
            render(it)
        }
        @Suppress("DEPRECATION") val track =
            intent.getSerializableExtra(Constants.TRACK) as Track

        showTrack(track)

        viewModel.preparePlayer(track.previewUrl)

        binding.buttonBack.setOnClickListener {
            finish()
        }

        binding.buttonPlay.apply {
            setOnClickListener {
                viewModel.playbackControl()
            }
        }

        binding.buttonLike.apply {
            setOnClickListener {
                if (viewModel.switchLike())
                    binding.buttonLike.background = AppCompatResources.getDrawable(
                        binding.root.context,
                        R.drawable.button_heart
                    )
                else binding.buttonLike.background = AppCompatResources.getDrawable(
                    binding.root.context,
                    R.drawable.button_heart_inactive
                )
            }
        }
        showTrack(track)
        binding.buttonPlay.isEnabled = false

    }

    private fun render(state: PlayerScreenState) {
        when (state) {
            is PlayerScreenState.Preparing -> {

            }

            is PlayerScreenState.Paused -> {
                binding.buttonPlay.background =
                    AppCompatResources.getDrawable(binding.root.context, R.drawable.button_play)
            }

            is PlayerScreenState.Playing -> {
                binding.buttonPlay.background =
                    AppCompatResources.getDrawable(binding.root.context, R.drawable.button_pause)
            }

            is PlayerScreenState.Unplayable -> {
                binding.buttonPlay.isEnabled = false
                binding.buttonPlay.background =
                    AppCompatResources.getDrawable(binding.root.context, R.drawable.button_play)
                binding.playTime.text = dateFormat.format(0)
            }

            is PlayerScreenState.Stopped -> {
                binding.buttonPlay.isEnabled = true
                binding.buttonPlay.background =
                    AppCompatResources.getDrawable(binding.root.context, R.drawable.button_play)
                binding.playTime.text = dateFormat.format(0)
            }

            is PlayerScreenState.TimerUpdating -> {
                binding.playTime.text = state.time
            }
        }
    }

    private fun showTrack(track: Track) {

        binding.apply {
            Glide
                .with(imageTrackTrack)
                .load(track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
                .placeholder(R.drawable.icon_no_picture)
                .centerCrop()
                .transform(
                    RoundedCorners(
                        dpToPx(2.0F, context = binding.root.context)
                    )
                )
                .into(imageTrackTrack)


            trackName.text = track.trackName
            trackName.isSelected = true
            artistName.text = track.artistName
            trackName.isSelected = true
            genre.text = track.primaryGenreName
            country.text = track.country

            duration.text =
                SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)

            val date =
                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(track.releaseDate)
            if (date != null) {
                val formattedDatesString =
                    SimpleDateFormat("yyyy", Locale.getDefault()).format(date)
                year.text = formattedDatesString
            }

            if (track.collectionName.isNotEmpty()) {
                album.text = track.collectionName
            } else {
                album.visibility = View.GONE
            }
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
    }


    fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics
        ).toInt()
    }
}