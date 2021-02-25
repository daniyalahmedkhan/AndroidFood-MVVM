package com.androidfood.mvvm.data.repo.restaurant_menus

import com.androidfood.mvvm.data.model.Restaurant.AddRestaurantResponse
import com.androidfood.mvvm.data.model.menus.RestaurantMenu
import com.androidfood.mvvm.data.retrofit.RetroInterface
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject

class RestaurantMenuRepo @Inject constructor(private val baseApiInterface: RetroInterface) {

    suspend fun getMenus(token: String, ID: Int): Response<RestaurantMenu> {
        return baseApiInterface.getRestaurantMenu(token, ID)
    }

    suspend fun addMenu(
        token: String,
        file: MultipartBody.Part,
        name: RequestBody,
        des: RequestBody,
        restaurant_id: RequestBody
    ): Response<Object> {
        return baseApiInterface.AddRestaurant(token, file, name, des, restaurant_id)
    }

    suspend fun createRestaurant(
        token: String,
        category_id: RequestBody,
        name: RequestBody,
        desc: RequestBody,
        phone: RequestBody,
        media: List<MultipartBody.Part>,
        address: RequestBody,
        lat: RequestBody,
        lng: RequestBody,
        url: RequestBody,
        timing: RequestBody,
        price: RequestBody,
        services: RequestBody
    ): Response<AddRestaurantResponse> {
        return baseApiInterface.createRestaurant(
            token, category_id, name, desc, phone, media, address, lat, lng, url, timing,
            price, services
        )
    }


    suspend fun updateRestaurant(
        token: String,
        id: Int,
        category_id: Int,
        name: String,
        desc: String,
        phone: String,
        address: String,
        lat: String,
        lng: String,
        url: String,
        timing: String,
        price: String,
        services: String
    ): Response<AddRestaurantResponse> {
        return baseApiInterface.updateRestaurant(
            token, id,  category_id, name, desc, phone, address, lat, lng, url, timing,
            price, services
        )
    }

    suspend fun deleteRestaurant(token: String, id: Int): Response<Object> {
        return baseApiInterface.deleteRestaurant(token, id)
    }
}