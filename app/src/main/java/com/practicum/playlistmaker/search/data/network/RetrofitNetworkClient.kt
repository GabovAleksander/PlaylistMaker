package com.practicum.playlistmaker.search.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.practicum.playlistmaker.Constants
import com.practicum.playlistmaker.search.data.NetworkClient
import com.practicum.playlistmaker.search.data.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient(private val context: Context): NetworkClient {
    private val iTunesbasedUrl = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesbasedUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val iTunesService = retrofit.create(ITunesAPI::class.java)

    override fun doRequest(dto: Any): Response {
        if(!isConnected()){
            return Response().apply { resultCode=Constants.NO_INTERNET_CONNECTION_CODE }
        }
        return  if (dto is TracksRequest) {
            val resp = iTunesService.findTrack(dto.expression).execute()
            val body= resp.body()?:Response()
            body.apply { resultCode=resp.code() }

        } else {
            return Response().apply { resultCode=Constants.BAD_REQUEST_CODE }
        }
    }

    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }
}