package com.practicum.playlistmaker.data.dto

class TracksResponse (
    val searchType: String,
    val count: Int,
    val results: List<TrackDto>
): Response()