package com.androidfood.mvvm.callback

import android.view.View

/**
 * Created by Daniyal Ahmed on 11/4/2020.
 */
interface ActionCallback {
    fun onBindingClick(view: View)
    fun onBindingClick(id: Int)
}