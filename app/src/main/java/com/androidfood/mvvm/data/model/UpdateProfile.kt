package com.androidfood.mvvm.data.model

import com.google.gson.annotations.SerializedName
import com.androidfood.mvvm.data.model.auth.Register.Details

data class UpdateProfile(

    @SerializedName("success") val success: Boolean,
    @SerializedName("data") val data: Details,
    @SerializedName("message") val message: String
)