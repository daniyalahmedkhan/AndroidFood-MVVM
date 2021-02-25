package com.androidfood.mvvm.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.androidfood.mvvm.R
import com.androidfood.mvvm.base.BaseActivity

class AuthActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        replaceFrag(LoginFragment.newInstance())
    }

    private fun replaceFrag(newFragment: Fragment) {
        replaceFragment(
            newFragment,
            R.id.auth_container,
            addBackStack = false,
            clearBackStack = true
        )
    }
}