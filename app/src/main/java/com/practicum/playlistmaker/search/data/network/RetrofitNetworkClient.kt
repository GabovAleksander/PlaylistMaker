package com.practicum.playlistmaker.search.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.practicum.playlistmaker.Constants
import com.practicum.playlistmaker.search.data.NetworkClient
import com.practicum.playlistmaker.search.data.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient(private val service: ITunesAPI, private val checker: ConnectionChecker) :
    NetworkClient {

    override suspend fun doRequest(dto: Any): Response {
        if (!checker.isConnected()) {
            return Response().apply { resultCode = Constants.NO_INTERNET_CONNECTION_CODE }
        }
        if (dto !is TracksRequest) {
            return Response().apply { resultCode = Constants.BAD_REQUEST_CODE }
        }
        return withContext(Dispatchers.IO) {
            try {
                val response = service.findTrack(dto.expression)
                response.apply { resultCode = Constants.SUCCESS_CODE }
            } catch (e: Throwable) {
                Response().apply { resultCode = Constants.INTERNAL_SERVER_ERROR }
            }
        }
    }
}