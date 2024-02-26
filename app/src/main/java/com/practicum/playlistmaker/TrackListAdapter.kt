package com.practicum.playlistmaker

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class TrackListAdapter(var items: ArrayList<Track>) :
    RecyclerView.Adapter<TrackListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackListViewHolder = TrackListViewHolder(parent)

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: TrackListViewHolder, position: Int) {
        holder.bind(items.get(position))
    }

}