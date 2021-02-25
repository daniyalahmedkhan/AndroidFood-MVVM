package com.androidfood.mvvm.repo.home

import com.androidfood.mvvm.Room.Dao

import com.androidfood.mvvm.Room.Tables.SearchItems
import com.androidfood.mvvm.data.model.searches.SearchResponse
import com.androidfood.mvvm.data.retrofit.RetroInterface
import retrofit2.Response
import javax.inject.Inject


class SearchRepo @Inject constructor(val dao: Dao , private val baseApiInterface: RetroInterface){

    suspend fun insertItemDB(model: SearchItems) : Long{
        return dao.insertSearchItem(model)
    }

    suspend fun getItemDB() : List<SearchItems>{
        return  dao.getSearchItems()
    }

    suspend fun getSearchItem(token: String) : Response<SearchResponse>{
        return  baseApiInterface.getRestaurantSearcher(token)
    }
}