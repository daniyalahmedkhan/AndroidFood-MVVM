package com.androidfood.mvvm.data.model.searches

import com.google.gson.annotations.SerializedName

data class SearchResponseList(
    @SerializedName("id") val id: Int,
    @SerializedName("user_id") val user_id: Int,
    @SerializedName("keyword") val keyword: String,
    @SerializedName("updated_at") val updated_at: String )