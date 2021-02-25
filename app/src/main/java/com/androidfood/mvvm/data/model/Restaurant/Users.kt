package com.androidfood.mvvm.data.model.Restaurant

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Users(
    @SerializedName("success") val success: String,
    @SerializedName("data") val data: UsersDataList?
) : Serializable