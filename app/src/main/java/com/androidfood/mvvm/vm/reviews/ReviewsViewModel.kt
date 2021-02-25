package com.androidfood.mvvm.vm.reviews

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androidfood.mvvm.data.model.reviews.AddReview
import com.androidfood.mvvm.data.model.reviews.GetReviews
import com.androidfood.mvvm.data.remote.GeneralApiResponse
import com.androidfood.mvvm.data.repo.reviews.ReviewsRepo
import com.androidfood.mvvm.helper.GeneralHelper
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.lang.Exception
import java.util.*

class ReviewsViewModel @ViewModelInject constructor(private val reviewsRepo: ReviewsRepo) :
    ViewModel() {

    val AddReviewState: MutableLiveData<GeneralApiResponse<AddReview>> = MutableLiveData()
    val GetReviewState: MutableLiveData<GeneralApiResponse<GetReviews>> = MutableLiveData()
    val UpdateReviewState: MutableLiveData<GeneralApiResponse<AddReview>> = MutableLiveData()
    val DeleteReviewState: MutableLiveData<GeneralApiResponse<Objects>> = MutableLiveData()
    private val token: String = "Bearer " + GeneralHelper.getUsersData().data.user.accessToken


    fun addReview(toID: Int, rating: Double, review: String) {
        viewModelScope.launch {
            try {
                AddReviewState.value = GeneralApiResponse.Loading
                val response = reviewsRepo.addReview(token, toID, rating, review)

                if (response.isSuccessful) {
                    AddReviewState.value = GeneralApiResponse.Success(response.body())
                } else {
                    val message =
                        GeneralHelper.parseFailureJson(JSONObject(response.errorBody().toString()))
                    AddReviewState.value = GeneralApiResponse.Failure(message)
                }

            } catch (e: Exception) {
                AddReviewState.value = GeneralApiResponse.Failure(e.toString())
            }
        }
    }


    fun getReviews() {
        viewModelScope.launch {
            try {

                GetReviewState.value = GeneralApiResponse.Loading
                val response = reviewsRepo.getReviews(token)

                if (response.isSuccessful) {
                    GetReviewState.value = GeneralApiResponse.Success(response.body())
                } else {
                    val message =
                        GeneralHelper.parseFailureJson(JSONObject(response.errorBody().toString()))
                    GetReviewState.value = GeneralApiResponse.Failure(message)
                }

            } catch (e: Exception) {
                GetReviewState.value = GeneralApiResponse.Failure(e.toString())
            }
        }
    }

    fun updateReview(id: Int, rating: Double, review: String) {
        viewModelScope.launch {
            try {

                UpdateReviewState.value = GeneralApiResponse.Loading
                val response = reviewsRepo.updateReview(token, id, rating, review)

                if (response.isSuccessful) {
                    UpdateReviewState.value = GeneralApiResponse.Success(response.body())
                } else {
                    val message =
                        GeneralHelper.parseFailureJson(JSONObject(response.errorBody().toString()))
                    UpdateReviewState.value = GeneralApiResponse.Failure(message)
                }

            } catch (e: Exception) {
                UpdateReviewState.value = GeneralApiResponse.Failure(e.toString())
            }
        }
    }


    fun deleteReview(id: Int) {
        viewModelScope.launch {
            try {

                DeleteReviewState.value = GeneralApiResponse.Loading
                val response = reviewsRepo.deleteReview(token, id)

                if (response.isSuccessful) {
                    DeleteReviewState.value = GeneralApiResponse.Success(response.body())
                } else {
                    val message =
                        GeneralHelper.parseFailureJson(JSONObject(response.errorBody().toString()))
                    DeleteReviewState.value = GeneralApiResponse.Failure(message)
                }

            } catch (e: Exception) {
                DeleteReviewState.value = GeneralApiResponse.Failure(e.toString())
            }
        }
    }


}