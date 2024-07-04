package com.practicum.playlistmaker.search.data.network

import com.google.gson.annotations.SerializedName
import com.practicum.playlistmaker.search.data.Response
import com.practicum.playlistmaker.player.domain.TrackDto
import com.practicum.playlistmaker.search.domain.Track

class TracksResponse(val results: ArrayList<TrackDto>) : Response()