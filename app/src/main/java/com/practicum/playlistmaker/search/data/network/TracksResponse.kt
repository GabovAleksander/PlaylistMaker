package com.practicum.playlistmaker.search.data.network

import com.practicum.playlistmaker.search.data.Response
import com.practicum.playlistmaker.player.domain.TrackDto
import com.practicum.playlistmaker.search.domain.Track

class TracksResponse (
    val searchType: String,
    val count: Int,
    val results: List<Track>, resultCode: Int
): Response()