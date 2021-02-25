package com.androidfood.mvvm.data.repo.favorites

import com.androidfood.mvvm.data.retrofit.RetroInterface
import retrofit2.Response
import javax.inject.Inject

class FavoritesRepo @Inject constructor(val baseApiInterface: RetroInterface) {


    suspend fun favRestaurant(token: String, restaurantID: Int): Response<Object> {
        return baseApiInterface.favoritesRestaurant(token , restaurantID)
    }

}