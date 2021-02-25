package com.androidfood.mvvm.data.repo.auth

import com.androidfood.mvvm.data.model.auth.ForgetPassword
import com.androidfood.mvvm.data.model.auth.LoginModel
import com.androidfood.mvvm.data.model.auth.Register.UserRegistrationModel
import com.androidfood.mvvm.data.model.auth.SocialLogin
import com.androidfood.mvvm.data.retrofit.RetroInterface
import retrofit2.Response
import java.util.*
import javax.inject.Inject

class LoginRepo @Inject constructor(private val baseApiInterface: RetroInterface){

    suspend fun userLogin(loginModel: LoginModel) : Response<UserRegistrationModel>{
        return baseApiInterface.loginUser(loginModel.email , loginModel.password , loginModel.device_type , loginModel.device_token)
    }

    suspend fun userSocialLogin(socialLogin: SocialLogin) : Response<UserRegistrationModel>{
        return baseApiInterface.socialLogin(socialLogin.platform , socialLogin.client_id, socialLogin.token , socialLogin.username ,
        socialLogin.email, socialLogin.image , socialLogin.device_token , socialLogin.device_type)
    }

    suspend fun userForgetPassword(email: String) : Response<ForgetPassword>{
        return baseApiInterface.forgetPassword(email)
    }

    suspend fun verifyCode(code: String) : Response<UserRegistrationModel>{
        return baseApiInterface.verifyResetCode(code)
    }

    suspend fun resetPassword(verificationCode: String, email: String, password: String, password_confirmation: String): Response<UserRegistrationModel>{
        return baseApiInterface.resetPassword(verificationCode, email, password , password_confirmation)
    }

    suspend fun logout(token: String) : Response<Objects>{
        return baseApiInterface.logout(token)

    }

    suspend fun changePassword(token:String , currentPassword:String, password:String, confirmPassword:String) : Response<UserRegistrationModel>{
        return baseApiInterface.changePassword(token, currentPassword, password, confirmPassword)
    }
}