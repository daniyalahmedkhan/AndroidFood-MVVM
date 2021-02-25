package com.androidfood.mvvm.utils

sealed class GeneralResponse<out R> {
    object Loading : GeneralResponse<Nothing>()
    class Failure(val error: String?) : GeneralResponse<Nothing>()
    data class Success<out T> (val list: T) : GeneralResponse<T>()
    data class SuccessVal<out T> (val int: T) : GeneralResponse<T>()

}