package com.androidfood.mvvm.data.model.Restaurant


import com.google.gson.annotations.SerializedName
import com.androidfood.mvvm.data.model.auth.Register.Restaurant.Restaurant
import java.io.Serializable

data class UsersDataList (

    @SerializedName("restaurants") val restaurants : List<Restaurant>,
//    @SerializedName("name") val name : String,
//    @SerializedName("email") val email : String,
//    @SerializedName("created_at") val created_at : String,
//    @SerializedName("roles_csv") val roles_csv : String,
//    @SerializedName("total_reviews") val total_reviews : Int,
//    @SerializedName("total_favorites") val total_favorites : Int,
//    @SerializedName("is_favorite") val is_favorite : Int,
//    @SerializedName("details") val details : Details
) : Serializable