package com.androidfood.mvvm.data.model.reviews

import com.google.gson.annotations.SerializedName
import com.androidfood.mvvm.data.model.auth.Register.User

data class AddReview(
    @SerializedName("data") val reviewData: ReviewData,
    @SerializedName("user") val user: User
)