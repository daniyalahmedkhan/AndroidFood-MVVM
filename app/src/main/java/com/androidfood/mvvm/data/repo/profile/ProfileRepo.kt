package com.androidfood.mvvm.data.repo.profile

import com.androidfood.mvvm.data.model.UpdateProfile
import com.androidfood.mvvm.data.retrofit.RetroInterface
import retrofit2.Response
import javax.inject.Inject

class ProfileRepo @Inject constructor(private val baseApiInterface: RetroInterface) {

    suspend fun updateProfile(token:String , name:String, phone:String, address:String) : Response<UpdateProfile>{
        return baseApiInterface.updateProfile(token , name, phone , address)
    }

}