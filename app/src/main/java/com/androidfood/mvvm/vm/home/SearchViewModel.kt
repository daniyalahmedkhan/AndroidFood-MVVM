package com.androidfood.mvvm.vm.home.home


import androidx.lifecycle.ViewModel
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.androidfood.mvvm.Room.Tables.SearchItems
import com.androidfood.mvvm.data.model.searches.SearchResponse
import com.androidfood.mvvm.data.remote.GeneralApiResponse
import com.androidfood.mvvm.helper.GeneralHelper
import com.androidfood.mvvm.repo.home.SearchRepo

import com.androidfood.mvvm.utils.GeneralResponse
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.lang.Exception


class SearchViewModel @ViewModelInject constructor(val searchRepo: SearchRepo) : ViewModel() {


//    @Inject
//  private  lateinit var searchRepo: SearchRepo

    val dbTranState: MutableLiveData<GeneralResponse<Long>> = MutableLiveData()
    val searchResponseState: MutableLiveData<GeneralApiResponse<SearchResponse>> = MutableLiveData()
    val dbTranValue: MutableLiveData<GeneralResponse<List<SearchItems>>> = MutableLiveData()
    val token: String = "Bearer " + GeneralHelper.getUsersData().data.user.accessToken

    fun insertInDB(item: String) {
        viewModelScope.launch {
            val searchItems = SearchItems(null, item, GeneralHelper.getCurrentTime().toString())
            dbTranState.value = GeneralResponse.SuccessVal(searchRepo.insertItemDB(searchItems))
        }
    }

    fun searchItem() {
        viewModelScope.launch {
            try {

                val response = searchRepo.getSearchItem(token)

                if (response.isSuccessful) {

                    searchResponseState.value = GeneralApiResponse.Success(response.body())

                } else {
                    searchResponseState.value = GeneralApiResponse.Failure(
                        GeneralHelper.parseFailureJson(
                            JSONObject(response.errorBody().toString())
                        )
                    )
                }


            } catch (e: Exception) {
                searchResponseState.value = GeneralApiResponse.Failure(e.toString())
            }
        }
    }

}