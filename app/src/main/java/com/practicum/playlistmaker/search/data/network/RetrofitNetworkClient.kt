package com.practicum.playlistmaker.search.data.network

import com.practicum.playlistmaker.search.data.NetworkClient
import com.practicum.playlistmaker.search.data.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RetrofitNetworkClient(private val service: ITunesAPI, private val checker: ConnectionChecker) :
    NetworkClient {

    override suspend fun doRequest(dto: Any): Response {
        if (!checker.isConnected()) {
            return Response().apply { resultCode = NetworkClient.NO_INTERNET_CONNECTION_CODE }
        }
        if (dto !is TracksRequest) {
            return Response().apply { resultCode = NetworkClient.BAD_REQUEST_CODE }
        }
        return withContext(Dispatchers.IO) {
            try {
                val response = service.findTrack(dto.expression)
                response.apply { resultCode = NetworkClient.SUCCESS_CODE }
            } catch (e: Throwable) {
                Response().apply { resultCode = NetworkClient.INTERNAL_SERVER_ERROR }
            }
        }
    }
}