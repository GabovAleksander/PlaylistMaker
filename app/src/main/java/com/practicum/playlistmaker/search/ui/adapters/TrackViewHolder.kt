package com.practicum.playlistmaker.search.ui.adapters

import android.content.Context
import android.util.TypedValue
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.internal.ViewUtils.dpToPx
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.TrackViewBinding
import com.practicum.playlistmaker.player.domain.TrackDto
import com.practicum.playlistmaker.search.domain.Track
import com.practicum.playlistmaker.search.model.mapToTrackDto
import java.text.SimpleDateFormat
import java.util.Locale

class TrackViewHolder(private val binding: TrackViewBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(track: Track) {
        binding.trackName.text = track.trackName
        binding.artistName.text = track.artistName
        binding.trackTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)

        Glide
            .with(itemView)
            .load(track.artworkUrl100)
            .placeholder(R.drawable.icon_no_picture)
            .transform(RoundedCorners(dpToPx(2.0F, itemView.context)))
            .into(binding.artwork)
        binding.artistName.requestLayout()
    }

    fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics
        ).toInt()
    }
}