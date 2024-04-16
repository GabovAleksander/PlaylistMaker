package com.practicum.playlistmaker.ui

import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class TrackListViewHolder(parentView: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parentView.context).inflate(R.layout.track_view, parentView, false)
) {
    private val trackName = super.itemView.findViewById<TextView>(R.id.trackName)
    private val artistName = super.itemView.findViewById<TextView>(R.id.artistName)
    private val trackTime = super.itemView.findViewById<TextView>(R.id.trackTime)
    private val artworkUrl100 = super.itemView.findViewById<ImageView>(R.id.artwork)

    fun bind(item: Track, clickListener: (Track) -> Unit) {
        trackName.text = item.trackName
        artistName.text = item.artistName.trim()
        trackTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(item.trackTimeMillis)
        Glide.with(itemView)
            .load(item.artworkUrl100)
            .placeholder(R.drawable.icon_no_picture)
            .centerInside()
            .transform(RoundedCorners(dpToPx(2.0F, itemView.context)))
            .into(artworkUrl100)
        artistName.requestLayout()
        itemView.setOnClickListener{clickListener(item)}
    }

    fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics
        ).toInt()
    }
}