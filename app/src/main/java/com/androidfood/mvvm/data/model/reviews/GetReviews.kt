package com.androidfood.mvvm.data.model.reviews

import com.google.gson.annotations.SerializedName

data class GetReviews(
    @SerializedName("data") val reviewData: List<ReviewData>
)