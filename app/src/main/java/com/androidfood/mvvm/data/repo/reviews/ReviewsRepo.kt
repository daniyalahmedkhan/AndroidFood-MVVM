package com.androidfood.mvvm.data.repo.reviews

import com.androidfood.mvvm.data.model.reviews.AddReview
import com.androidfood.mvvm.data.model.reviews.GetReviews
import com.androidfood.mvvm.data.retrofit.RetroInterface
import retrofit2.Response
import java.util.*
import javax.inject.Inject

class ReviewsRepo @Inject constructor(private val baseApiInterface: RetroInterface) {

    suspend fun addReview(
        token: String,
        toId: Int,
        rating: Double,
        review: String
    ): Response<AddReview> {
        return baseApiInterface.addReview(token, toId, rating, review)
    }

    suspend fun getReviews(token: String): Response<GetReviews> {
        return baseApiInterface.getReviews(token)
    }

    suspend fun updateReview(
        token: String,
        id: Int,
        rating: Double,
        review: String
    ): Response<AddReview> {
        return baseApiInterface.updateReview(token, id, rating, review)
    }

    suspend fun deleteReview(token: String, id: Int): Response<Objects> {
        return baseApiInterface.deleteReview(token, id)
    }
}