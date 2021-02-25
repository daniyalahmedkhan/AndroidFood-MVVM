package com.androidfood.mvvm.vm.restaurant_menu

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androidfood.mvvm.data.model.Restaurant.AddRestaurantResponse
import com.androidfood.mvvm.data.model.menus.RestaurantMenu
import com.androidfood.mvvm.data.remote.GeneralApiResponse
import com.androidfood.mvvm.data.repo.restaurant_menus.RestaurantMenuRepo
import com.androidfood.mvvm.helper.GeneralHelper
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import java.lang.Exception

class RestaurantMenuViewModel @ViewModelInject constructor(private val restaurantMenuRepo: RestaurantMenuRepo) :
    ViewModel() {

    val getMenusState: MutableLiveData<GeneralApiResponse<RestaurantMenu>> = MutableLiveData()
    val addMenusState: MutableLiveData<GeneralApiResponse<Object>> = MutableLiveData()
    val deleteRestaurantState: MutableLiveData<GeneralApiResponse<Object>> = MutableLiveData()
    val createResState: MutableLiveData<GeneralApiResponse<AddRestaurantResponse>> =
        MutableLiveData()
    val token: String = "Bearer " + GeneralHelper.getUsersData().data.user.accessToken

    fun getMenus(ID: Int) {

        viewModelScope.launch {
            try {
                getMenusState.value = GeneralApiResponse.Loading
                val resposne = restaurantMenuRepo.getMenus(token, ID)
                if (resposne.isSuccessful) {

                    getMenusState.value = GeneralApiResponse.Success(resposne.body())

                } else {
                    val message =
                        GeneralHelper.parseFailureJson(JSONObject(resposne.errorBody().toString()))
                    getMenusState.value = GeneralApiResponse.Failure(message)
                }
            } catch (e: Exception) {
                getMenusState.value = GeneralApiResponse.Failure(e.toString())
            }


        }

    }

    fun addMenu(
        file: MultipartBody.Part,
        name: RequestBody,
        des: RequestBody,
        restaurant_id: RequestBody
    ) {
        viewModelScope.launch {
            try {
                addMenusState.value = GeneralApiResponse.Loading
                val response = restaurantMenuRepo.addMenu(token, file, name, des, restaurant_id)
                if (response.isSuccessful) {
                    addMenusState.value = GeneralApiResponse.Success(response.body())
                } else {
                    val message =
                        GeneralHelper.parseFailureJson(JSONObject(response.errorBody().toString()))
                    addMenusState.value = GeneralApiResponse.Failure(message)
                }
            } catch (e: Exception) {
                addMenusState.value = GeneralApiResponse.Failure(e.toString())
            }
        }

    }

    fun createRestaurant(
        category_id: RequestBody,
        name: RequestBody,
        desc: RequestBody,
        phone: RequestBody,
        media: ArrayList<MultipartBody.Part>,
        address: RequestBody,
        lat: RequestBody,
        lng: RequestBody,
        url: RequestBody,
        timing: RequestBody,
        price: RequestBody,
        services: RequestBody
    ) {
        viewModelScope.launch {
            try {
                createResState.value = GeneralApiResponse.Loading
                val response = restaurantMenuRepo.createRestaurant(
                    token, category_id, name, desc, phone, media, address,
                    lat, lng, url, timing, price, services
                )
                if (response.isSuccessful) {
                    createResState.value = GeneralApiResponse.Success(response.body())
                } else {
                    val msg =
                        GeneralHelper.parseFailureJson(JSONObject(response.errorBody()?.string()))
                    createResState.value = GeneralApiResponse.Failure(msg)
                }
            } catch (e: Exception) {
                createResState.value = GeneralApiResponse.Failure(e.toString())
            }
        }
    }

    fun updateRestaurant(
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
    ) {
        viewModelScope.launch {
            try {
                createResState.value = GeneralApiResponse.Loading
                val response = restaurantMenuRepo.updateRestaurant(
                    token, id, category_id, name, desc, phone, address,
                    lat, lng, url, timing, price, services
                )
                if (response.isSuccessful) {
                    createResState.value = GeneralApiResponse.Success(response.body())
                } else {
                    val msg =
                        GeneralHelper.parseFailureJson(JSONObject(response.errorBody()?.string()))
                    createResState.value = GeneralApiResponse.Failure(msg)
                }
            } catch (e: Exception) {
                createResState.value = GeneralApiResponse.Failure(e.toString())
            }
        }
    }

    fun deleteRestaurant(id: Int) {
        viewModelScope.launch {
            try {
                deleteRestaurantState.value = GeneralApiResponse.Loading
                val response = restaurantMenuRepo.deleteRestaurant(token, id)
                if (response.isSuccessful) {

                    deleteRestaurantState.value = GeneralApiResponse.Success(response.body())

                } else {
                    val msg =
                        GeneralHelper.parseFailureJson(JSONObject(response.errorBody()?.string()))
                    deleteRestaurantState.value = GeneralApiResponse.Failure(msg)
                }
            } catch (e: Exception) {
                deleteRestaurantState.value = GeneralApiResponse.Failure(e.toString())
            }
        }
    }


}