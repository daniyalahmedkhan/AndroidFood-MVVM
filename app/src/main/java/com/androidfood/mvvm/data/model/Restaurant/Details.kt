package com.androidfood.mvvm.data.model.Restaurant


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Details (

	@SerializedName("id") val id : Int,
	@SerializedName("user_id") val user_id : Int,
	@SerializedName("first_name") val first_name : String,
	@SerializedName("last_name") val last_name : String,
	@SerializedName("short_name") val short_name : String,
	@SerializedName("phone") val phone : String,
	@SerializedName("address") val address : String,
	@SerializedName("image") val image : String,
	@SerializedName("average_rating") val average_rating : Double,
	@SerializedName("latitude") val latitude : Double,
	@SerializedName("longitude") val longitude : Double,
	@SerializedName("url") val url : String,
	@SerializedName("types") val types : String,
	@SerializedName("email_updates") val email_updates : Int,
	@SerializedName("is_social_login") val is_social_login : Int,
	@SerializedName("image_url") val image_url : String
) : Serializable