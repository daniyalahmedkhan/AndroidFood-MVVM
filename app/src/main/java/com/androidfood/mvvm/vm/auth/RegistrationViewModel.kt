package com.androidfood.mvvm.vm.auth

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.material.textfield.TextInputLayout
import com.androidfood.mvvm.data.model.auth.Register.UserRegistrationModel
import com.androidfood.mvvm.data.remote.GeneralApiResponse
import com.androidfood.mvvm.data.repo.auth.RegistrationRepo
import com.androidfood.mvvm.helper.GeneralHelper
import kotlinx.coroutines.launch
import org.json.JSONObject

class RegistrationViewModel @ViewModelInject constructor(val registrationRepo: RegistrationRepo) :
    ViewModel() {

    val registrationState: MutableLiveData<GeneralApiResponse<UserRegistrationModel>> =
        MutableLiveData()
    private var userRegistrationModel: UserRegistrationModel = UserRegistrationModel()

    fun handleUserInput(textInputLayout: Array<TextInputLayout>) {
        for (textInput in textInputLayout) {
            if (textInput.editText?.text.toString().isEmpty()) {
                registrationState.value = GeneralApiResponse.Failure("Please fill all details")
                return
            } else {
                if (textInput.hint!!.contains("name", true)) {
                    userRegistrationModel.name = textInput.editText?.text.toString().trim()
                } else if (textInput.hint!!.contains("phone", true)) {
                    userRegistrationModel.phone = textInput.editText?.text.toString().trim()
                } else if (textInput.hint!!.contains("email", true)) {
                    userRegistrationModel.email = textInput.editText?.text.toString().trim()
                } else if (textInput.hint!!.contains("address", true)) {
                    userRegistrationModel.address = textInput.editText?.text.toString().trim()
                } else if (textInput.hint!!.contains("confirm", true)) {
                    userRegistrationModel.password_confirmation =
                        textInput.editText?.text.toString().trim()
                } else if (textInput.hint!!.contains("password", true)) {
                    userRegistrationModel.password = textInput.editText?.text.toString().trim()
                }
            }
        }

        userRegistrationModel.device_token = "123"
        userRegistrationModel.device_type = "android"
        userRegistrationModel.role = "3"
        userRegistration(userRegistrationModel)
    }


    private fun userRegistration(userRegistrationModel: UserRegistrationModel) {
        viewModelScope.launch {
            try {
                val response = registrationRepo.userRegistration(userRegistrationModel)
                if (response.isSuccessful) {
                    registrationState.value = GeneralApiResponse.Success(response.body())
                } else {
                    val jObjError = JSONObject(response.errorBody()!!.string())
                    val message = GeneralHelper.parseFailureJson((jObjError))
                    registrationState.value = GeneralApiResponse.Failure(message)
                }
            } catch (e: Exception) {
                registrationState.value = GeneralApiResponse.Failure(e.toString())
            }

        }

    }
}