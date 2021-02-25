package com.androidfood.mvvm.data.model.Restaurant

import com.google.gson.annotations.SerializedName

data class AddRestaurantResponse (

    @SerializedName("success") val success : Boolean,
    @SerializedName("data") val data : AddRestaurantResponseDetail,
    @SerializedName("message") val message : String
)