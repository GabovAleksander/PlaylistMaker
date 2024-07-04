package com.practicum.playlistmaker.search.data.impl

import com.practicum.playlistmaker.Constants
import com.practicum.playlistmaker.Resource
import com.practicum.playlistmaker.search.data.NetworkClient
import com.practicum.playlistmaker.search.data.SharedPreferencesHistoryStorage
import com.practicum.playlistmaker.search.data.network.TracksRequest
import com.practicum.playlistmaker.search.data.network.TracksResponse
import com.practicum.playlistmaker.search.domain.Track
import com.practicum.playlistmaker.search.domain.TracksRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class TracksRepositoryImpl(
    private val networkClient: NetworkClient, private val localStorage:
    SharedPreferencesHistoryStorage
) : TracksRepository {

    override fun searchTracks(expression: String): Flow<Resource<ArrayList<Track>>> = flow{

        val response = networkClient.doRequest(
            TracksRequest(
                expression
            )
        )
        when (response.resultCode) {
            Constants.NO_INTERNET_CONNECTION_CODE -> {
                emit(Resource.Error(message = Constants.INTERNET_CONNECTION_ERROR))
            }

            Constants.SUCCESS_CODE -> {
                val arrayListTracks = arrayListOf<Track>()
                (response as TracksResponse).results.forEach {
                    arrayListTracks.add(
                        Track(
                            it.trackId,
                            it.trackName,
                            it.artistName,
                            it.trackTimeMillis,
                            it.artworkUrl100,
                            it.previewUrl,
                            it.collectionName,
                            it.releaseDate,
                            it.primaryGenreName,
                            it.country,
                        )
                    )
                }
                emit(Resource.Success(arrayListTracks))
            }

            else -> {
                emit(Resource.Error(message = Constants.SERVER_ERROR))
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