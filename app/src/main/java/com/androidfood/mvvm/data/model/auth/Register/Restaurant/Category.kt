package com.androidfood.mvvm.data.model.auth.Register.Restaurant;

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class Category (

	@SerializedName("id") val id : Int,
	@SerializedName("name") val name : String,
	@SerializedName("created_at") val created_at : String,
	@SerializedName("updated_at") val updated_at : String,
	@SerializedName("deleted_at") val deleted_at : String
) : Serializable