package com.androidfood.mvvm.vm.profile

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.material.textfield.TextInputLayout
import com.androidfood.mvvm.data.model.UpdateProfile
import com.androidfood.mvvm.data.remote.GeneralApiResponse
import com.androidfood.mvvm.data.repo.profile.ProfileRepo
import com.androidfood.mvvm.helper.GeneralHelper
import kotlinx.coroutines.launch
import org.json.JSONObject

class ProfileViewModel @ViewModelInject constructor(val profileRepo: ProfileRepo) : ViewModel() {

    var profileUpdateState: MutableLiveData<GeneralApiResponse<UpdateProfile>> =
        MutableLiveData()
    private var token: String = GeneralHelper.getUsersData().data.user.accessToken


    fun updateProfile(textInputLayout: Array<TextInputLayout>) {
        viewModelScope.launch {
            try {

                profileUpdateState.value = GeneralApiResponse.Loading
                if (GeneralHelper.checkInput(textInputLayout)) {
                    val response = profileRepo.updateProfile(
                        "Bearer "+token,
                        textInputLayout.get(0).editText?.text.toString(),
                        textInputLayout.get(1).editText?.text.toString(),
                        textInputLayout.get(2).editText?.text.toString(),
                    )

                    if (response.isSuccessful) {

                        profileUpdateState.value = GeneralApiResponse.Success(response.body())

                    } else {
                        val message = GeneralHelper.parseFailureJson(
                            JSONObject(
                                response.errorBody().toString()
                            )
                        )
                        profileUpdateState.value =
                            GeneralApiResponse.Failure(message)
                    }

                } else {
                    profileUpdateState.value =
                        GeneralApiResponse.Failure("Please fill all required data")
                }

            } catch (e: Exception) {
                profileUpdateState.value = GeneralApiResponse.Failure(e.toString())
            }
        }
    }

}