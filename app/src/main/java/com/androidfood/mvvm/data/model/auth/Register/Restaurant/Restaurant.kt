package com.androidfood.mvvm.data.model.auth.Register.Restaurant;

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class Restaurant (

	@SerializedName("id") val id : Int,
	@SerializedName("user_id") val user_id : Int,
	@SerializedName("category_id") val category_id : Int,
	@SerializedName("name") val name : String,
	@SerializedName("description") val description : String,
	@SerializedName("phone") val phone : String,
	@SerializedName("average_rating") val average_rating : Int,
	@SerializedName("address") val address : String,
	@SerializedName("latitude") val latitude : Double,
	@SerializedName("longitude") val longitude : Double,
	@SerializedName("url") val url : String,
	@SerializedName("timing") val timing : String,
	@SerializedName("price_range") val price_range : String,
	@SerializedName("services") val services : String,
	@SerializedName("created_at") val created_at : String,
	@SerializedName("updated_at") val updated_at : String,
	@SerializedName("deleted_at") val deleted_at : String,
	@SerializedName("is_reviewed") val is_reviewed : Int,
	@SerializedName("is_favorite") val is_favorite : Int,
	@SerializedName("total_reviews") val total_reviews : Int,
	@SerializedName("total_favorites") val total_favorites : Int,
	@SerializedName("category") val category : Category,
	@SerializedName("attachments") val attachments : List<Attachments>
) : Serializable