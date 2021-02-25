package com.androidfood.mvvm.data.model.auth

data class SocialLogin(
    val platform: String?,
    val client_id: String?,
    val token: String?,
    val username: String?,
    val email: String?,
    val image: String?,
    val device_token: String?,
    val device_type: String?
)