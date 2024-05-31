package com.practicum.playlistmaker.search.domain

import java.io.Serializable

data class Track(
    val trackId: Int,
    val trackName: String, // Название композиции
    val artistName: String, // Имя исполнителя
    val trackTimeMillis: Long, // Продолжительность трека
    val artworkUrl100: String, // Ссылка на изображение обложки
    val previewUrl: String, //Ссылка на отрывок трека
    val collectionName: String, // Название альбома
    val releaseDate: String, // Год релиза
    val primaryGenreName: String, // Жанр
    val country: String // Страна
) : Serializable {
    override fun equals(other: Any?): Boolean {
        return if (other !is Track) {
            false
        } else {
            other.trackId == trackId
        }
    }

    override fun hashCode(): Int {
        return trackId
    }
}

