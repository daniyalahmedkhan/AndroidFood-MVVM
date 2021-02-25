package com.androidfood.mvvm.data.model.auth.Register.Restaurant;

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class Attachments (

	@SerializedName("id") val id : Int,
	@SerializedName("name") val name : String,
	@SerializedName("path") val path : String,
	@SerializedName("attachment_url") val attachment_url : String
) : Serializable