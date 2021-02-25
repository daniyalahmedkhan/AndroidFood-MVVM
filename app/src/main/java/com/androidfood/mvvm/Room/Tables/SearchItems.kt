package com.androidfood.mvvm.Room.Tables

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.androidfood.mvvm.utils.DateTimeConverter


@Entity
data class SearchItems(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int? = 0,
    val name: String,
    @TypeConverters(DateTimeConverter::class)
    val dateTime: String
)