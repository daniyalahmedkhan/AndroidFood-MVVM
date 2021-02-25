package com.androidfood.mvvm.base

import android.annotation.SuppressLint
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by Daniyal Ahmed on 11/4/2020.
 */
@AndroidEntryPoint
@SuppressLint("Registered")
abstract class BaseActivity : AppCompatActivity() {
    protected inline fun <reified T : ViewDataBinding> binding(
        @LayoutRes resId: Int
    ): Lazy<T> = lazy { DataBindingUtil.setContentView<T>(this, resId) }

    fun replaceFragment(
        fragment: Fragment,
        frameId: Int,
        addBackStack: Boolean,
        clearBackStack: Boolean
    ) {
        supportFragmentManager.transact {
            if (addBackStack)
                addToBackStack(fragment::class.java.name)
            if (clearBackStack) {
                for (i in 0 until supportFragmentManager.backStackEntryCount) {
                    supportFragmentManager.popBackStack()
                }
            }
            replace(frameId, fragment)
        }
    }

    private inline fun FragmentManager.transact(action: FragmentTransaction.() -> Unit) {
        beginTransaction().apply {
            action()
        }.commit()
    }
}