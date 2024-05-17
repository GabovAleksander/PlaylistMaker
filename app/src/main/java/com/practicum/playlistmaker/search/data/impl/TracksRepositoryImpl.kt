package com.practicum.playlistmaker.search.data.impl

import com.practicum.playlistmaker.Constants
import com.practicum.playlistmaker.Resource
import com.practicum.playlistmaker.search.data.NetworkClient
import com.practicum.playlistmaker.search.data.network.TracksRequest
import com.practicum.playlistmaker.search.data.network.TracksResponse
import com.practicum.playlistmaker.search.data.storage.LocalStorage
import com.practicum.playlistmaker.search.domain.Track
import com.practicum.playlistmaker.search.domain.TracksRepository
import com.practicum.playlistmaker.search.model.mapToTrack
import com.practicum.playlistmaker.search.model.mapToTrackDto

import kotlin.math.exp

class TracksRepositoryImpl(
    private val networkClient: NetworkClient, private val localStorage:
    LocalStorage
) : TracksRepository {

    override fun searchTracks(expression: String): Resource<List<Track>> {

        val response = networkClient.doRequest(
            TracksRequest(
                expression
            )
        )
        return when (response.resultCode) {
            Constants.NO_INTERNET_CONNECTION_CODE -> {
                Resource.Error(
                    message = Constants.INTERNET_CONNECTION_ERROR,
                    code = Constants.NO_INTERNET_CONNECTION_CODE
                )
            }
            Constants.SUCCESS_CODE -> {
                Resource.Success((response as TracksResponse).results.map {
                    it
                }, code = Constants.SUCCESS_CODE)
            }
            else -> {
                Resource.Error(message = Constants.SERVER_ERROR, code = response.resultCode)
            }
        }
    }

    override fun addTrackToHistory(track: Track) {
        localStorage.addToHistory(track)
    }

    override fun clearHistory() {
        localStorage.clearHistory()
    }

    override fun getHistory(): ArrayList<Track> {
        return localStorage.getHistory()
    }
}