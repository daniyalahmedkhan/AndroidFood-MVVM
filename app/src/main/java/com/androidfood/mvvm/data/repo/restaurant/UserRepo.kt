package com.androidfood.mvvm.data.repo.restaurant

import com.androidfood.mvvm.data.model.Restaurant.Users
import com.androidfood.mvvm.data.retrofit.RetroInterface
import retrofit2.Response
import javax.inject.Inject

class UserRepo @Inject constructor(private val baseApiInterface: RetroInterface) {

    suspend fun getUserRestaurant(token: String): Response<Users> {
        return baseApiInterface.getRestaurant(token)
    }

    suspend fun getUserRestaurantList(
        token: String,
        lat: Double?,
        lng: Double?,
        category: String
    ): Response<Users> {
        return baseApiInterface.getRestaurantList(token, lat, lng, category)
    }


    suspend fun getFavUserRestaurant(token: String, fav: Boolean) : Response<Users>{
        return  baseApiInterface.getFavRestaurantList(token , fav)
    }
}