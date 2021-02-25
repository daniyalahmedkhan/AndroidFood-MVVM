package com.androidfood.mvvm.data.model.Restaurant

import com.google.gson.annotations.SerializedName

data class AddRestaurantResponseDetail(

    @SerializedName("category_id") val category_id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("address") val address: String,
    @SerializedName("latitude") val latitude: String,
    @SerializedName("longitude") val longitude: String,
    @SerializedName("url") val url: String,
    @SerializedName("timing") val timing: String,
    @SerializedName("price_range") val price_range: String,
    @SerializedName("services") val services: String,
    @SerializedName("user_id") val user_id: Int,
    @SerializedName("updated_at") val updated_at: String,
    @SerializedName("created_at") val created_at: String,
    @SerializedName("id") val id: Int,
    @SerializedName("is_reviewed") val is_reviewed: Int,
    @SerializedName("is_favorite") val is_favorite: Int,
    @SerializedName("total_reviews") val total_reviews: Int,
    @SerializedName("total_favorites") val total_favorites: Int
)