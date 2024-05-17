package com.practicum.playlistmaker.ui

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.search.domain.Track

class TrackListAdapter(var items: MutableList<Track>, private val clickListener: (Track) -> Unit) :
    RecyclerView.Adapter<TrackListViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackListViewHolder = TrackListViewHolder(parent)

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: TrackListViewHolder, position: Int) {
        holder.bind(items.get(position), clickListener)
    }


}