package com.androidfood.mvvm.data.model.menus

import com.google.gson.annotations.SerializedName

data class RestaurantMenu(
    @SerializedName("data") val data: List<GetMenus>
)

