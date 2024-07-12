package com.practicum.playlistmaker.search.domain.Impl

import com.practicum.playlistmaker.Resource
import com.practicum.playlistmaker.search.domain.ResponseDataStatus
import com.practicum.playlistmaker.search.domain.Track
import com.practicum.playlistmaker.search.domain.TracksInteractor
import com.practicum.playlistmaker.search.domain.TracksRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.concurrent.Executors

class TracksInteractorImpl(private val repository: TracksRepository) : TracksInteractor {


    override fun searchTracks(query: String):Flow<ResponseDataStatus> {

        return repository.searchTracks(query).map{result ->
            when (result) {
                is Resource.Success -> {
                    ResponseDataStatus(result.data, null)
                }

                is Resource.Error -> {
                    ResponseDataStatus(null, result.message)
                }
            }
        }
    }

    override fun addTrackToHistory(track: Track) {
        repository.addTrackToHistory(track)
    }

    override fun clearHistory() {
        repository.clearHistory()
    }

    override fun getHistory(): ArrayList<Track> {
        return repository.getHistory()
    }
}