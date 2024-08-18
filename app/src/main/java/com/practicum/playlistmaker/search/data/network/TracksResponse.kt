package com.practicum.playlistmaker.search.data.network

import com.practicum.playlistmaker.search.data.Response
import com.practicum.playlistmaker.player.data.TrackDto

class TracksResponse(val results: ArrayList<TrackDto>) : Response()