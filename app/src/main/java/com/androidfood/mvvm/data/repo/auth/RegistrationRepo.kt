package com.androidfood.mvvm.data.repo.auth

import com.androidfood.mvvm.data.model.auth.Register.UserRegistrationModel
import com.androidfood.mvvm.data.retrofit.RetroInterface
import retrofit2.Response
import javax.inject.Inject

class RegistrationRepo @Inject constructor(val baseApiInterface: RetroInterface){

    suspend fun userRegistration(userRegistrationModel: UserRegistrationModel) : Response<UserRegistrationModel>{
        return baseApiInterface.registerUser(userRegistrationModel.name , userRegistrationModel.address , userRegistrationModel.phone ,
        "" , userRegistrationModel.email , userRegistrationModel.password , userRegistrationModel.password_confirmation ,
        userRegistrationModel.role , userRegistrationModel.device_token , userRegistrationModel.device_type)
    }

}


