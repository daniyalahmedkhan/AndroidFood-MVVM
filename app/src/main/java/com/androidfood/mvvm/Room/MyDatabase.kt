package com.androidfood.mvvm.Room


import androidx.room.Database
import androidx.room.RoomDatabase
import com.androidfood.mvvm.Room.Tables.SearchItems


@Database(entities = [SearchItems::class] , version = 1 , exportSchema = false)
abstract class MyDatabase : RoomDatabase() {
    abstract fun dao(): Dao
}