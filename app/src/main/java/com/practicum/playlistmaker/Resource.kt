package com.practicum.playlistmaker

sealed class Resource<T> (val data: T? = null, val code: Int, val message: String? = null){
    class Success<T>(data: T, code: Int): Resource<T>(data, code = code)
    class Error<T>(data: T? = null, code: Int, message: String): Resource<T>(data, code, message)
}