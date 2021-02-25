package com.androidfood.mvvm.ui.profile

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.androidfood.mvvm.R
import com.androidfood.mvvm.base.BaseActivity
import com.androidfood.mvvm.callback.ActionCallback
import com.androidfood.mvvm.data.remote.GeneralApiResponse
import com.androidfood.mvvm.databinding.ActivityAboutBinding
import com.androidfood.mvvm.databinding.ActivityChangePassowordBinding
import com.androidfood.mvvm.helper.GeneralHelper
import com.androidfood.mvvm.vm.auth.LoginViewModel
import kotlinx.android.synthetic.main.activity_change_passoword.*


class ChangePassoword : BaseActivity(), ActionCallback {

    val loginViewModel: LoginViewModel by viewModels()

    private val binding: ActivityChangePassowordBinding by binding(R.layout.activity_change_passoword)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.backArrow.setOnClickListener {
            finish()
        }


        binding.BtnChangePass.setOnClickListener {

            loginViewModel.changePassword(arrayOf(currentPass, newPass, confirmNewPass))
        }

        loginViewModel.changePasswordState.observe(this, Observer {
            when (it) {

                is GeneralApiResponse.Loading -> {
                    loadingView.visibility = View.VISIBLE
                }
                is GeneralApiResponse.Failure -> {
                    loadingView.visibility = View.GONE
                    GeneralHelper.showDialog(this, it.error, object : ActionCallback {
                        override fun onBindingClick(view: View) {
                        }

                        override fun onBindingClick(id: Int) {
                        }

                    })
                }

                is GeneralApiResponse.Success -> {
                    loadingView.visibility = View.GONE
                    finish()
                }
            }
        })
    }

    override fun onBindingClick(view: View) {
        TODO("Not yet implemented")
    }

    override fun onBindingClick(id: Int) {
        TODO("Not yet implemented")
    }

}