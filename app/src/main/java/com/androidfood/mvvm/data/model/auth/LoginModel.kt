package com.androidfood.mvvm.data.model.auth

data class LoginModel constructor(
    val email: String?,
    val password: String?,
    val device_type: String?,
    val device_token: String?
)



