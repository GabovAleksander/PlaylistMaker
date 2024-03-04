package com.practicum.playlistmaker

import android.content.SharedPreferences
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class TrackListAdapter(var items: MutableList<Track>, sharedPreferences: SharedPreferences) :
    RecyclerView.Adapter<TrackListViewHolder>() {

    val searchHistory = SearchHistory(sharedPreferences)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackListViewHolder = TrackListViewHolder(parent)

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: TrackListViewHolder, position: Int) {
        holder.bind(items.get(position))
        holder.itemView.setOnClickListener{
            searchHistory.saveTrack(items.get(position))
        }
    }

}