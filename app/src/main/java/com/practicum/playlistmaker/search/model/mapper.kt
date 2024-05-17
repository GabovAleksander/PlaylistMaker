package com.practicum.playlistmaker.search.model

import com.practicum.playlistmaker.player.domain.TrackDto
import com.practicum.playlistmaker.search.domain.Track

fun Track.mapToTrackDto()=TrackDto(trackId, trackName,artistName,trackTimeMillis,artworkUrl100,previewUrl,collectionName,releaseDate,primaryGenreName,country)
fun TrackDto.mapToTrack()=Track(trackId, trackName,artistName,trackTimeMillis,artworkUrl100,previewUrl,collectionName,releaseDate,primaryGenreName,country)