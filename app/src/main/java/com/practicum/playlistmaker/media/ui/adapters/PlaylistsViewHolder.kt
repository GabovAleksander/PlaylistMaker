package com.practicum.playlistmaker.media.ui.adapters

import android.os.Environment
import android.view.View
import android.widget.ImageView
import android.widget.TextView
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
    itemView: View
) : RecyclerView.ViewHolder(itemView) {

    private val playlistCover: ImageView = itemView.findViewById(R.id.cover_playlist)
    private val playlistName: TextView = itemView.findViewById(R.id.name_playlist)
    private val tracksCount: TextView = itemView.findViewById(R.id.count_tracks)

    fun bind(playlist: Playlist) {
        val cornerRadius = itemView.resources.getDimensionPixelSize(R.dimen.corner_radius_8)
        playlistName.text = playlist.name
        tracksCount.text = tracksCount.resources.getQuantityString(
            R.plurals.tracks, playlist.tracksCount, playlist.tracksCount
        )

        val filePath = File(
            itemView.context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            PLAYLISTS_IMAGES)
        Glide
            .with(itemView)
            .load(playlist.cover?.let { imageName -> File(filePath, imageName) })
            .placeholder(R.drawable.icon_no_picture_big)
            .transform(CenterCrop(), RoundedCorners(cornerRadius))
            .into(playlistCover)

    }
    companion object{
        val PLAYLISTS_IMAGES="playlist_images"
    }
}