package com.practicum.playlistmaker.search.data

interface NetworkClient {
    suspend fun doRequest(dto: Any): Response

    companion object{
        internal const val SUCCESS_CODE = 200
        internal const val NO_INTERNET_CONNECTION_CODE = -1
        internal const val INTERNET_CONNECTION_ERROR = "Проверьте подключение к интернету"
        internal const val SERVER_ERROR = "Ошибка сервера"
        internal const val BAD_REQUEST_CODE = 400
        internal const val INTERNAL_SERVER_ERROR = 500
    }
}