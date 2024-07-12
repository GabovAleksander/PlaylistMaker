package com.practicum.playlistmaker.player.ui

import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlayerBinding
import com.practicum.playlistmaker.search.domain.Track
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerFragment : Fragment() {
    val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }
    private lateinit var binding: FragmentPlayerBinding
    private val viewModel by viewModel<PlayerViewModel>()
    private lateinit var track: Track

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        track = viewModel.getTrack()
        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }

        showTrack(track)

        viewModel.preparePlayer(track.previewUrl)

        binding.buttonPlay.setOnClickListener {
                binding.buttonPlay.startAnimation(android.view.animation.AnimationUtils.loadAnimation(requireContext(), R.anim.scale))
                viewModel.playbackControl()
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

        binding.buttonBack.setOnClickListener {
            findNavController().navigateUp()
        }
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
                .load(track.artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg"))
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
                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())?.parse(track.releaseDate)
            if (date != null) {
                val formattedDatesString =
                    SimpleDateFormat("yyyy", Locale.getDefault()).format(date)
                year.text = formattedDatesString
            }

            if ((track.collectionName?:"").isNotEmpty()) {
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