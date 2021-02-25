package com.androidfood.mvvm.app

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.androidfood.mvvm.helper.PrefsHelper
import dagger.hilt.android.HiltAndroidApp

/**
 * Created by Daniyal Ahmed on 11/4/2020.
 */
@HiltAndroidApp
class AppController : Application() {
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        ctx = this

        PrefsHelper.Builder()
            .setContext(this)
            .setMode(MODE_PRIVATE)
            .setPrefsName(packageName)
            .setUseDefaultSharedPreference(true)
            .build()

    }

    companion object {
        val TAG: String = AppController::class.java
            .simpleName
        lateinit var ctx: AppController
        fun getAppContext(): AppController {
            return ctx
        }
    }
}