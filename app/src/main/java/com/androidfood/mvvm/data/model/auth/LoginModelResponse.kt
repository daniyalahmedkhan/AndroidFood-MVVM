package com.androidfood.mvvm.data.model.auth

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LoginModelResponse(
    @Expose
    @SerializedName("success") val response: String,
    @Expose
    @SerializedName("message") val message: String,
    @Expose
    @SerializedName("data") val loginModelData: LoginModelData
)

data class LoginModelData(val loginModelUser: LoginModelUser)

data class LoginModelUser(
    val id: String?,
    val name: String?
)