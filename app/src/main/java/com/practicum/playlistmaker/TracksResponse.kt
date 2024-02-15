package com.practicum.playlistmaker

class TracksResponse (val searchType: String,
    val count: Int,
    val results: List<Track>
)