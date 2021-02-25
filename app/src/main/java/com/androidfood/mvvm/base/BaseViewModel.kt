package com.androidfood.mvvm.base

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by Daniyal Ahmed on 11/4/2020.
 */
abstract class BaseViewModel : ViewModel() {
    private var mCompositeDisposable: CompositeDisposable? = null

    protected open fun getCompositeDisposable(): CompositeDisposable? {
        if (mCompositeDisposable == null) mCompositeDisposable = CompositeDisposable()
        return mCompositeDisposable
    }

    override fun onCleared() {
        getCompositeDisposable()!!.clear()
        super.onCleared()
    }
}