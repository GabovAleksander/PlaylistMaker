package com.practicum.playlistmaker.di

import android.content.Context
import android.media.MediaPlayer
import androidx.room.Room
import com.google.gson.Gson
import com.practicum.playlistmaker.media.data.db.DataBase
import com.practicum.playlistmaker.media.data.db.entity.RoomMapper
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
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.factory
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


val dataModule = module {

    single<ITunesAPI> {
        val ITUNES_URL="https://itunes.apple.com"
        val CALL_TIMEOUT = 30
        val READ_TIMEOUT = 30
        val okHttpClient = OkHttpClient.Builder()
            .callTimeout(CALL_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(ITUNES_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
        retrofit.create(ITunesAPI::class.java)
    }


    single {
        androidContext().getSharedPreferences(
            HistoryStorage.HISTORY_KEY,
            Context.MODE_PRIVATE
        )
    }

    single {
        Room
            .databaseBuilder(androidContext(), DataBase::class.java, "database.db")
            .fallbackToDestructiveMigration()
            .build()
    }
    factory { Gson() }

    singleOf(::SharedPreferencesHistoryStorage).bind<HistoryStorage>()

    singleOf(::ConnectionChecker)

    singleOf(::RetrofitNetworkClient).bind<NetworkClient>()

    factoryOf (::RoomMapper)

    factory<PlayerClient> {
        Player(client = get())
    }

    factory {
        MediaPlayer()
    }

    singleOf(::SharedPreferencesThemeStorage).bind<ThemeStorage>()

}
