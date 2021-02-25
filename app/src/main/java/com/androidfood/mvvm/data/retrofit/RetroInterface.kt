package com.androidfood.mvvm.data.retrofit


import com.androidfood.mvvm.data.model.Restaurant.AddRestaurantResponse
import com.androidfood.mvvm.data.model.Restaurant.Users
import com.androidfood.mvvm.data.model.UpdateProfile
import com.androidfood.mvvm.data.model.auth.ForgetPassword
import com.androidfood.mvvm.data.model.auth.Register.UserRegistrationModel
import com.androidfood.mvvm.data.model.menus.RestaurantMenu
import com.androidfood.mvvm.data.model.reviews.AddReview
import com.androidfood.mvvm.data.model.reviews.GetReviews
import com.androidfood.mvvm.data.model.searches.SearchResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*
import java.util.*

interface RetroInterface {


    @FormUrlEncoded
    @POST("user-registration")
    suspend fun registerUser(
        @Field("name") name: String,
        @Field("address") address: String,
        @Field("phone") phone: String,
        @Field("image") image: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("password_confirmation") password_confirmation: String,
        @Field("role") role: String,
        @Field("device_token") device_token: String,
        @Field("device_type") device_type: String, ): Response<UserRegistrationModel>


    @FormUrlEncoded
    @POST("login")
    suspend fun loginUser(
        @Field("email") email: String?,
        @Field("password") password: String?,
        @Field("device_type") device_type: String?,
        @Field("device_token") device_token: String?
    ): Response<UserRegistrationModel>


    @FormUrlEncoded
    @POST("social_login")
    suspend fun socialLogin(
        @Field("platform") platform: String?,
        @Field("client_id") client_id: String?,
        @Field("token") token: String?,
        @Field("username") username: String?,
        @Field("email") email: String?,
        @Field("image") image: String?,
        @Field("device_token") device_token: String?,
        @Field("device_type") device_type: String?
    ): Response<UserRegistrationModel>



    @GET("forget-password")
    suspend fun forgetPassword(@Query("email") email: String?): Response<ForgetPassword>

    @FormUrlEncoded
    @POST("verify-reset-code")
    suspend fun verifyResetCode(@Field("verification_code") code: String): Response<UserRegistrationModel>

    @FormUrlEncoded
    @POST("reset-password")
    suspend fun resetPassword(
        @Field("verification_code") verificationCode: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("password_confirmation") password_confirmation: String
    ): Response<UserRegistrationModel>

    @POST("logout")
    suspend fun logout(@Header("Authorization") token: String): Response<Objects>

    @FormUrlEncoded
    @POST("change-password")
    suspend fun changePassword(
        @Header("Authorization") token: String,
        @Field("current_password") current_password: String,
        @Field("password") password: String,
        @Field("password_confirmation") password_confirmation: String
    ): Response<UserRegistrationModel>


    // *** Profile

    @FormUrlEncoded
    @POST("profile")
    suspend fun updateProfile(
        @Header("Authorization") token: String,
        @Field("name") name: String,
        @Field("phone") phone: String,
        @Field("address") address: String
    ): Response<UpdateProfile>


    // single restaurant
    @GET("users")
    suspend fun getRestaurant(@Header("Authorization") token: String): Response<Users>


    // Home Restaurant
    @GET("restaurants")
    suspend fun getRestaurantList(
        @Header("Authorization") token: String,
        @Query("latitude") latitude: Double?,
        @Query("longitude") longitude: Double?,
        @Query("keyword") category: String
    ): Response<Users>

    // Fav Restaurant
    @GET("restaurants")
    suspend fun getFavRestaurantList(
        @Header("Authorization") token: String,
        @Query("is_favorite") fav: Boolean
    ): Response<Users>


    // Favorite and UnFavorite
    @FormUrlEncoded
    @POST("favorites")
    suspend fun favoritesRestaurant(
        @Header("Authorization") token: String,
        @Field("restaurant_id") id: Int
    ): Response<Object>


    /////////////// RESTAURANT MENU Start //////////// 24-01-2020

    // Restaurant Menus
    @GET("restaurant-menu-items")
    suspend fun getRestaurantMenu(
        @Header("Authorization") token: String,
        @Query("restaurant_id") restaurant_id: Int
    ): Response<RestaurantMenu>

    @Multipart
    @POST("restaurant-menu-items")
    suspend fun AddRestaurant(
        @Header("Authorization") token: String,
        @Part storyMedia: MultipartBody.Part,
        @Part("name") name: RequestBody,
        @Part("description") description: RequestBody,
        @Part("restaurant_id") restaurant_id: RequestBody
    ): Response<Object>


    @Multipart
    @POST("restaurants")
    suspend fun createRestaurant(
        @Header("Authorization") token: String,
        @Part("category_id") id: RequestBody,
        @Part("name") name: RequestBody,
        @Part("description") description: RequestBody,
        @Part("phone") phone: RequestBody,
        @Part images: List<MultipartBody.Part>,
        @Part("address") address: RequestBody,
        @Part("latitude") latitude: RequestBody,
        @Part("longitude") longitude: RequestBody,
        @Part("url") url: RequestBody,
        @Part("timing") timing: RequestBody,
        @Part("price_range") price_range: RequestBody,
        @Part("services") services: RequestBody
    ): Response<AddRestaurantResponse>

    @FormUrlEncoded
    @PUT("restaurants/{id}")
    suspend fun updateRestaurant(
        @Header("Authorization") token: String,
        @Path("id") i: Int,
        @Field("category_id") id: Int,
        @Field("name") name: String,
        @Field("description") description: String,
        @Field("phone") phone: String,
        @Field("address") address: String,
        @Field("latitude") latitude: String,
        @Field("longitude") longitude: String,
        @Field("url") url: String,
        @Field("timing") timing: String,
        @Field("price_range") price_range: String,
        @Field("services") services: String
    ): Response<AddRestaurantResponse>


    @DELETE("restaurants/{id}")
    suspend fun deleteRestaurant(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<Object>

    /////////////// RESTAURANT MENU End //////////// 24-01-2020


    @GET("user-searches")
    suspend fun getRestaurantSearcher(@Header("Authorization") token: String): Response<SearchResponse>


    /////////////// REVIEWS Start /////////////// 24-01-2020 DONE

    // Add Review
    @FormUrlEncoded
    @POST("reviews")
    suspend fun addReview(
        @Header("Authorization") token: String, @Field("restaurant_id") to_id: Int,
        @Field("rating") rating: Double, @Field("review") review: String
    ): Response<AddReview>

    // Get Reviews
    @GET("reviews")
    suspend fun getReviews(
        @Header("Authorization") token: String
    ): Response<GetReviews>

    // Update Reviews
    @FormUrlEncoded
    @PUT("reviews/{id}")
    suspend fun updateReview(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Field("rating") rating: Double,
        @Field("review") review: String
    ): Response<AddReview>

    // Delete Reviews
    @DELETE("reviews/{id}")
    suspend fun deleteReview(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<Objects>

    /////////////// REVIEWS End /////////////// 24-01-2020 DONE

}