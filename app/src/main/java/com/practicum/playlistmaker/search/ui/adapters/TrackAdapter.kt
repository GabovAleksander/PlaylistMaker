package com.practicum.playlistmaker.search.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.databinding.TrackViewBinding
import com.practicum.playlistmaker.search.domain.Track

class TrackAdapter(
    private val clickListener: TrackClickListener,
    private val longClickListener: LongTrackClickListener? = null) :
    RecyclerView.Adapter<TrackViewHolder>() {

    var tracks = listOf<Track>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        val binding = TrackViewBinding.inflate(layoutInspector, parent, false)
        return TrackViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener {
            clickListener.onTrackClick(tracks[holder.adapterPosition])
        }
        longClickListener?.let { listener ->
            holder.itemView.setOnLongClickListener {
                listener.onTrackLongClick(tracks[holder.adapterPosition])
                return@setOnLongClickListener true
            }
        }
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    fun interface TrackClickListener {
        fun onTrackClick(tracks: Track)
    }

    fun interface LongTrackClickListener {
        fun onTrackLongClick(track: Track)
    }
}