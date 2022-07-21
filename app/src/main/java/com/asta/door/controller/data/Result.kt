package com.asta.door.controller.data

sealed class Result<out R> {
    object Loading : Result<Nothing>()
    data class Success<out T>(val data: T? = null) : Result<T>()
    data class Error(val msg: String? = null) : Result<Nothing>()
}