package com.androidfood.mvvm.vm.favorites

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androidfood.mvvm.data.remote.GeneralApiResponse
import com.androidfood.mvvm.data.repo.favorites.FavoritesRepo
import com.androidfood.mvvm.helper.GeneralHelper
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.lang.Exception

class FavViewModel @ViewModelInject constructor(private val favoritesRepo: FavoritesRepo) :
    ViewModel() {

    val favRestaurantState: MutableLiveData<GeneralApiResponse<Object>> = MutableLiveData()
    val token: String = "Bearer "+GeneralHelper.getUsersData().data.user.accessToken


    fun favRestaurant(id: Int) {
        viewModelScope.launch {
            try {

                val response = favoritesRepo.favRestaurant(token, id)
                if (response.isSuccessful) {
                    favRestaurantState.value = GeneralApiResponse.Success(response.body())
                } else {
                    val message =
                        GeneralHelper.parseFailureJson(JSONObject(response.errorBody().toString()))
                    favRestaurantState.value = GeneralApiResponse.Failure(message)
                }

            } catch (e: Exception) {
                favRestaurantState.value = GeneralApiResponse.Failure(e.toString())
            }
        }
    }
}