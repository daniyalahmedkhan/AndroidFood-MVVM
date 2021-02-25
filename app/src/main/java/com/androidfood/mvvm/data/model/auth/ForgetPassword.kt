package com.androidfood.mvvm.data.model.auth

import com.google.gson.annotations.SerializedName

data class ForgetPassword(
    @SerializedName("success") val response: String,
    @SerializedName("message") val message: String
)