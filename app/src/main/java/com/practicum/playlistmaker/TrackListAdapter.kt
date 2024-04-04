package com.practicum.playlistmaker

import android.content.Intent
import android.content.SharedPreferences
import android.os.Handler
import android.os.Looper
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson

class TrackListAdapter(var items: MutableList<Track>, sharedPreferences: SharedPreferences) :
    RecyclerView.Adapter<TrackListViewHolder>() {

    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())
    val searchHistory = SearchHistory(sharedPreferences)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackListViewHolder = TrackListViewHolder(parent)

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: TrackListViewHolder, position: Int) {
        holder.bind(items.get(position))
        holder.itemView.setOnClickListener{

            if(clickDebounce()) {
                searchHistory.saveTrack(items.get(position))
                val context = holder.itemView.context
                val playerIntent = Intent(context, PlayerActivity::class.java)
                val gson = Gson()
                val json = gson.toJson(items.get(position))
                context.startActivity(playerIntent.putExtra(TRACK_KEY, json))
            }
        }
    }

    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }
    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}