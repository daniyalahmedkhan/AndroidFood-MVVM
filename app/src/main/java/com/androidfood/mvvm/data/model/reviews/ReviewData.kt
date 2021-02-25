package com.androidfood.mvvm.data.model.reviews

import com.google.gson.annotations.SerializedName
import com.androidfood.mvvm.data.model.auth.Register.User

data class ReviewData(@SerializedName("id") val id: Int?,
                      @SerializedName("from_id") val from_id: Int?,
                      @SerializedName("to_id") val from: Int?,
                      @SerializedName("rating") val rating: Double?,
                      @SerializedName("review") val review: String?,
                      @SerializedName("created_at") val created_at: String?,
                      @SerializedName("updated_at") val updated_at: String?,
                      @SerializedName("deleted_at") val deleted_at: String?,
                      @SerializedName("user") val user: User)