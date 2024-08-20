package com.practicum.playlistmaker.media.ui.adapters

import android.os.Environment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ItemPlaylistBinding
import com.practicum.playlistmaker.new_playlist.domain.models.Playlist
import com.practicum.playlistmaker.utils.setImage
import java.io.File

class PlaylistsViewHolder(
    private val binding: ItemPlaylistBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(model: Playlist) {
        val cornerRadius = itemView.resources.getDimensionPixelSize(R.dimen.corner_radius_8)

        binding.namePlaylist.text = model.name
        binding.countTracks.text = itemView.resources.getQuantityString(R.plurals.tracks, model.tracksCount, model.tracksCount)


        binding.namePlaylist.text = model.name
        binding.countTracks.text = itemView.resources.getQuantityString(R.plurals.tracks, model.tracksCount, model.tracksCount)


        val filePath = File(
            itemView.context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            PLAYLISTS_IMAGES)
        Glide
            .with(itemView)
            .load(model.cover?.let { imageName -> File(filePath, imageName) })
            .placeholder(R.drawable.icon_no_picture_big)
            .transform(CenterCrop(), RoundedCorners(cornerRadius))
            .into(binding.coverPlaylist)

    }
    companion object{
        val PLAYLISTS_IMAGES="playlist_images"
    }
}