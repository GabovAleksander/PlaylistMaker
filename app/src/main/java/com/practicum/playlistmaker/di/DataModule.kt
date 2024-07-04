package com.practicum.playlistmaker.di

import android.content.Context
import android.media.MediaPlayer
import com.google.gson.Gson
import com.practicum.playlistmaker.Constants
import com.practicum.playlistmaker.player.data.Player
import com.practicum.playlistmaker.player.data.PlayerClient
import com.practicum.playlistmaker.search.data.HistoryStorage
import com.practicum.playlistmaker.search.data.NetworkClient
import com.practicum.playlistmaker.search.data.network.ConnectionChecker
import com.practicum.playlistmaker.search.data.SharedPreferencesHistoryStorage
import com.practicum.playlistmaker.search.data.network.ITunesAPI
import com.practicum.playlistmaker.search.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.settings.preferences.SharedPreferencesThemeStorage
import com.practicum.playlistmaker.settings.preferences.ThemeStorage
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


val dataModule = module {

    single<ITunesAPI> {
        val okHttpClient = OkHttpClient.Builder()
            .callTimeout(Constants.CALL_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .readTimeout(Constants.READ_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.ITUNES_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
        retrofit.create(ITunesAPI::class.java)
    }


    single {
        androidContext().getSharedPreferences(
            Constants.HISTORY_KEY,
            Context.MODE_PRIVATE
        )
    }

    factory { Gson() }

    singleOf(::SharedPreferencesHistoryStorage).bind<HistoryStorage>()

    singleOf(::ConnectionChecker)

    singleOf(::RetrofitNetworkClient).bind<NetworkClient>()

    factory<PlayerClient> {
        Player(client = get())
    }

    factory {
        MediaPlayer()
    }

    singleOf(::SharedPreferencesThemeStorage).bind<ThemeStorage>()


}
