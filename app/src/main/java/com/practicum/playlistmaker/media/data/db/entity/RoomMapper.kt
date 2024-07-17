package com.practicum.playlistmaker.media.data.db.entity

import com.practicum.playlistmaker.search.domain.Track
import java.util.Calendar

class RoomMapper {
    fun map(track: TrackEntity): Track {
        return Track(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.previewUrl,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country
        )
    }

    fun map(track: Track): TrackEntity {
        return TrackEntity(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.previewUrl,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            Calendar.getInstance().timeInMillis
        )
    }
}