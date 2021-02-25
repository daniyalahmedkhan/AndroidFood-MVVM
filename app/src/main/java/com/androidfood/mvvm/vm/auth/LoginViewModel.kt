package com.androidfood.mvvm.vm.auth

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.material.textfield.TextInputLayout
import com.androidfood.mvvm.data.model.auth.ForgetPassword
import com.androidfood.mvvm.data.model.auth.LoginModel
import com.androidfood.mvvm.data.model.auth.Register.UserRegistrationModel
import com.androidfood.mvvm.data.model.auth.SocialLogin
import com.androidfood.mvvm.data.remote.GeneralApiResponse
import com.androidfood.mvvm.data.repo.auth.LoginRepo
import com.androidfood.mvvm.helper.GeneralHelper
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.*


class LoginViewModel @ViewModelInject constructor(val loginRepo: LoginRepo) : ViewModel() {

    val loginState: MutableLiveData<GeneralApiResponse<UserRegistrationModel>> = MutableLiveData()
    val forgetPasswordState: MutableLiveData<GeneralApiResponse<ForgetPassword>> = MutableLiveData()
    val codeVerificationState: MutableLiveData<GeneralApiResponse<UserRegistrationModel>> =
        MutableLiveData()
    val resetPasswordState: MutableLiveData<GeneralApiResponse<UserRegistrationModel>> =
        MutableLiveData()
    val changePasswordState: MutableLiveData<GeneralApiResponse<UserRegistrationModel>> =
        MutableLiveData()
    val logoutState: MutableLiveData<GeneralApiResponse<Objects>> =
        MutableLiveData()

    private lateinit var token: String


    fun handleUserInput(loginEmail: String, loginPassword: String) {
        loginState.value = GeneralApiResponse.Loading
        if (loginEmail.isEmpty() || loginPassword.isEmpty()) {
            loginState.value = GeneralApiResponse.Failure("Please provide all fields")
            return
        } else {
            userLogin(LoginModel(loginEmail, loginPassword, "android", "abc"))
        }
    }

    fun handleSocialLogin(socialLogin: SocialLogin) {
        userSocialLogin(socialLogin)
    }


    fun userLogin(loginModel: LoginModel) {
        viewModelScope.launch {
            loginState.value = GeneralApiResponse.Loading
            try {
                val response = loginRepo.userLogin(loginModel)
                if (response.isSuccessful) {
                    loginState.value = GeneralApiResponse.Success(response.body())
                } else {
                    loginState.value = GeneralApiResponse.Failure(response.message())
                }
            } catch (e: Exception) {
                loginState.value = GeneralApiResponse.Failure(e.toString())
            }

        }
    }

    fun userSocialLogin(socialLogin: SocialLogin) {
        viewModelScope.launch {
            loginState.value = GeneralApiResponse.Loading
            try {
                val response = loginRepo.userSocialLogin(socialLogin)
                if (response.isSuccessful) {
                    loginState.value = GeneralApiResponse.Success(response.body())
                } else {
                    loginState.value = GeneralApiResponse.Failure(response.message())
                }
            } catch (e: Exception) {
                loginState.value = GeneralApiResponse.Failure(e.toString())
            }

        }
    }


    fun userForgetPassword(email: String) {
        viewModelScope.launch {
            try {
                if (!email.isEmpty()) {
                    forgetPasswordState.value = GeneralApiResponse.Loading
                    val response = loginRepo.userForgetPassword(email)
                    if (response.isSuccessful) {
                        forgetPasswordState.value = GeneralApiResponse.Success(response.body())
                    } else {
                        val message = GeneralHelper.parseFailureJson(
                            JSONObject(
                                response.errorBody()?.string()
                            )
                        )
                        forgetPasswordState.value = GeneralApiResponse.Failure(message)
                    }
                } else {
                    forgetPasswordState.value = GeneralApiResponse.Failure("Please provide email")
                }


            } catch (e: Exception) {
                forgetPasswordState.value = GeneralApiResponse.Failure(e.toString())
            }
        }
    }

    fun userCodeVerification(code: TextInputLayout) {
        viewModelScope.launch {
            try {

                if (code.editText?.text.toString().isEmpty()) {
                    codeVerificationState.value = GeneralApiResponse.Failure("Please provide OTP")
                    return@launch
                }

                codeVerificationState.value = GeneralApiResponse.Loading
                val response = loginRepo.verifyCode(code.editText?.text.toString())
                if (response.isSuccessful) {
                    codeVerificationState.value = GeneralApiResponse.Success(response.body())
                } else {
                    val message =
                        GeneralHelper.parseFailureJson(JSONObject(response.errorBody()?.string()))
                    codeVerificationState.value = GeneralApiResponse.Failure(message)
                }

            } catch (e: Exception) {
                codeVerificationState.value = GeneralApiResponse.Failure(e.toString())
            }
        }
    }

    fun userResetPassword(textInputLayout: Array<TextInputLayout>) {
        viewModelScope.launch {
            try {
                resetPasswordState.value = GeneralApiResponse.Loading

                if (GeneralHelper.checkInput(textInputLayout)) {

                    val response = loginRepo.resetPassword(
                        textInputLayout.get(0).editText?.text.toString(),
                        textInputLayout.get(1).editText?.text.toString(),
                        textInputLayout.get(2).editText?.text.toString(),
                        textInputLayout.get(3).editText?.text.toString()
                    )

                    if (response.isSuccessful) {
                        resetPasswordState.value = GeneralApiResponse.Success(response.body())
                    } else {
                        val message = GeneralHelper.parseFailureJson(
                            JSONObject(
                                response.errorBody()?.string()
                            )
                        )
                        resetPasswordState.value = GeneralApiResponse.Failure(message)
                    }


                } else {
                    resetPasswordState.value =
                        GeneralApiResponse.Failure("Please provide all fields")
                    return@launch
                }


            } catch (e: Exception) {
                resetPasswordState.value = GeneralApiResponse.Failure(e.toString())
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            try {
                token = "Bearer " + GeneralHelper.getUsersData().data.user.accessToken
                logoutState.value = GeneralApiResponse.Loading
                val response = loginRepo.logout(token)
                if (response.isSuccessful) {
                    logoutState.value = GeneralApiResponse.Success(response.body())
                } else {
                    logoutState.value = GeneralApiResponse.Failure("Failed")
                }


            } catch (e: Exception) {
                logoutState.value = GeneralApiResponse.Failure("Failed")
            }
        }
    }

    fun changePassword(textInputLayout: Array<TextInputLayout>) {
        viewModelScope.launch {
            try {


                val response = loginRepo.changePassword(
                    token,
                    textInputLayout.get(0).editText?.text.toString(),
                    textInputLayout.get(1).editText?.text.toString(),
                    textInputLayout.get(2).editText?.text.toString()

                )

                if (response.isSuccessful) {
                    changePasswordState.value = GeneralApiResponse.Success(response.body())
                } else {
                    val message = GeneralHelper.parseFailureJson(
                        JSONObject(
                            response.errorBody()?.string()
                        )
                    )
                    resetPasswordState.value = GeneralApiResponse.Failure(message)
                }

            } catch (e: Exception) {
                changePasswordState.value = GeneralApiResponse.Failure(e.toString())
            }
        }
    }
}