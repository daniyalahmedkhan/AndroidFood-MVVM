package com.androidfood.mvvm.data.remote

sealed class GeneralApiResponse<out R> {
    object Loading : GeneralApiResponse<Nothing>()
    class Failure(val error: String?) : GeneralApiResponse<Nothing>()
    data class Success<out T> (val data: T?) : GeneralApiResponse<T>()

}