package com.androidfood.mvvm.vm.restaurant

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androidfood.mvvm.data.model.Restaurant.Users
import com.androidfood.mvvm.data.remote.GeneralApiResponse
import com.androidfood.mvvm.data.repo.restaurant.UserRepo
import com.androidfood.mvvm.helper.GeneralHelper
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.lang.Exception

class UserRestaurantViewModel @ViewModelInject constructor(val userRepo: UserRepo) : ViewModel() {

    var userResutaurantState: MutableLiveData<GeneralApiResponse<Users>> = MutableLiveData()
    private var token: String = GeneralHelper.getUsersData().data.user.accessToken
    //  private var token: String = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOlwvXC9zYW1oYXNlcnZpY2VzLmNvbVwvYXBwXC9hcGlcL3YxXC9sb2dpbiIsImlhdCI6MTYxMDg3MzMzOSwiZXhwIjoxNjQyNDA5MzM5LCJuYmYiOjE2MTA4NzMzMzksImp0aSI6ImJDOUU2R2FVOFU3ZzVoa08iLCJzdWIiOjYsInBydiI6IjIzYmQ1Yzg5NDlmNjAwYWRiMzllNzAxYzQwMDg3MmRiN2E1OTc2ZjcifQ.8-3MXI6nzn9udHFC9W-V30QE672q1eAT_GXs7SvGN5o"

    fun getUserRestaurant() {
        viewModelScope.launch {
            try {
                val response = userRepo.getUserRestaurant("Bearer " + token)

                if (response.isSuccessful) {

                    userResutaurantState.value = GeneralApiResponse.Success(response.body())

                } else {
                    val message =
                        GeneralHelper.parseFailureJson(JSONObject(response.errorBody().toString()))
                    userResutaurantState.value = GeneralApiResponse.Failure(message)
                }


            } catch (e: Exception) {
                userResutaurantState.value = GeneralApiResponse.Failure(e.toString())
            }
        }
    }

    fun getUserRestaurantList(lat: Double?, lng: Double?, category: String) {
        viewModelScope.launch {
            try {
                userResutaurantState.value = GeneralApiResponse.Loading

                val response = userRepo.getUserRestaurantList("Bearer " + token, lat, lng, category)

                if (response.isSuccessful) {

                    userResutaurantState.value = GeneralApiResponse.Success(response.body())

                } else {
                    val message =
                        GeneralHelper.parseFailureJson(JSONObject(response.errorBody().toString()))
                    userResutaurantState.value = GeneralApiResponse.Failure(message)
                }


            } catch (e: Exception) {
                userResutaurantState.value = GeneralApiResponse.Failure(e.toString())
            }
        }
    }

    fun getUserFavRestaurantList(fav: Boolean) {
        viewModelScope.launch {
            try {
                userResutaurantState.value = GeneralApiResponse.Loading

                val response = userRepo.getFavUserRestaurant("Bearer " + token, fav)

                if (response.isSuccessful) {

                    userResutaurantState.value = GeneralApiResponse.Success(response.body())

                } else {
                    val message =
                        GeneralHelper.parseFailureJson(JSONObject(response.errorBody().toString()))
                    userResutaurantState.value = GeneralApiResponse.Failure(message)
                }


            } catch (e: Exception) {
                userResutaurantState.value = GeneralApiResponse.Failure(e.toString())
            }
        }
    }
}