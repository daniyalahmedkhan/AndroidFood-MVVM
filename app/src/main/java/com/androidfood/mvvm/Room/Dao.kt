package com.androidfood.mvvm.Room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.androidfood.mvvm.Room.Tables.SearchItems

@Dao
interface Dao {

    @Insert
    suspend fun insertSearchItem(searchItems: SearchItems) : Long

    @Query("Select * from SearchItems order by id desc")
    suspend fun getSearchItems() : List<SearchItems>


}