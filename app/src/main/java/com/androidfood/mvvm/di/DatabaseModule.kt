package com.androidfood.mvvm.di

import android.app.Application

import androidx.room.Room
import com.androidfood.mvvm.Room.Dao
import com.androidfood.mvvm.Room.MyDatabase


import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun getDatabase(application: Application): MyDatabase {
        return Room.databaseBuilder(application , MyDatabase::class.java , "MakanMana").allowMainThreadQueries().fallbackToDestructiveMigration().build()
    }

    @Singleton
    @Provides
    fun getDatabaseDAO(myDatabase: MyDatabase): Dao {
        return myDatabase.dao()
    }
}