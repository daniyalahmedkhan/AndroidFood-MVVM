package com.androidfood.mvvm.data.model.menus

import com.google.gson.annotations.SerializedName

data class GetMenus(
    @SerializedName("id") val id: Int,
    @SerializedName("user_id") val user_id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("image") val image: String,
    @SerializedName("description") val description: String,
    @SerializedName("image_url") val image_url: String
)