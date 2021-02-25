package com.androidfood.mvvm.data.model.searches

import com.google.gson.annotations.SerializedName

data class SearchResponse(@SerializedName("data") val searchList: List<SearchResponseList>)