package com.practicum.playlistmaker.media.ui.adapters

import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ItemPlaylistBinding
import com.practicum.playlistmaker.new_playlist.domain.models.Playlist
import com.practicum.playlistmaker.utils.setImage

class PlaylistsViewHolder(
    private val binding: ItemPlaylistBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(model: Playlist) {
        val cornerRadius = itemView.resources.getDimensionPixelSize(R.dimen.corner_radius_8)

        binding.namePlaylist.text = model.playlistName
        binding.countTracks.text = itemView.resources.getQuantityString(R.plurals.tracks, model.tracksCount, model.tracksCount)

        binding.coverPlaylist.setImage(
            url = model.coverImageUrl,
            placeholder = R.drawable.icon_no_picture_big,
            cornerRadius = cornerRadius,
        )
    }
}