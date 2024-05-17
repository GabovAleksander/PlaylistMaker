package com.practicum.playlistmaker.player.ui

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.res.ResourcesCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.App
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityPlayerBinding
import com.practicum.playlistmaker.player.domain.TrackDto
import java.text.SimpleDateFormat
import java.util.Locale

sealed class PlayerState {
    val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }

    class BeginningState(val track: TrackDto): PlayerState() {

        override fun render(binding: ActivityPlayerBinding) {

            Glide.with(binding.imageTrackTrack)
                .load(track.getCoverArtwork())
                .placeholder(R.drawable.icon_no_picture)
                .centerInside()
                .transform(RoundedCorners(dpToPx(2.0F, context = binding.root.context)))
                .into(binding.imageTrackTrack)



            binding.trackName.text = track.trackName
            binding.artistName.text = track.artistName
            binding.duration.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
            binding.album.text = track.collectionName
            binding.year.text = track.releaseDate.substring(0, 4)
            binding.genre.text = track.primaryGenreName
            binding.country.text = track.country
            binding.playTime.text = dateFormat.format(0)
        }
    }

    class PlayButtonHandling(private val playerState: PlayerViewModel.PlayerStateCodes) : PlayerState() {
        override fun render(binding: ActivityPlayerBinding) {
            when (playerState) {
                PlayerViewModel.PlayerStateCodes.STATE_PLAYING -> {
                    binding.buttonPlay.background= AppCompatResources.getDrawable(binding.root.context,R.drawable.button_pause)
                }
                else -> {
                    binding.buttonPlay.background=AppCompatResources.getDrawable(binding.root.context,R.drawable.button_play)
                }
            }
        }
    }

    class Preparing: PlayerState(){
        override fun render(binding: ActivityPlayerBinding) {
            binding.buttonPlay.isEnabled = true
        }
    }
    class TimerUpdating(private val time: String): PlayerState(){
        override fun render(binding: ActivityPlayerBinding) {
            binding.playTime.text = time
        }
    }
    class PlayCompleting: PlayerState(){
        override fun render(binding: ActivityPlayerBinding) {
            binding.playTime.text = dateFormat.format(0)
            binding.buttonPlay.background=AppCompatResources.getDrawable(binding.root.context,R.drawable.button_play)
        }
    }


    fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics
        ).toInt()
    }
    abstract fun render(binding: ActivityPlayerBinding)
}