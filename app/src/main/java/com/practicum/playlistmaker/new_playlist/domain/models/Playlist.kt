package com.practicum.playlistmaker.new_playlist.domain.models

import com.practicum.playlistmaker.search.domain.Track

data class Playlist(
    val id: Int,
    val coverImageUrl: String,
    val playlistName: String,
    val playlistDescription:String,
    var trackList: List<Track>,
    var tracksCount: Int,
)