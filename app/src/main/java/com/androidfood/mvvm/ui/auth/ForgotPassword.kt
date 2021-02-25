package com.androidfood.mvvm.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.androidfood.mvvm.R
import com.androidfood.mvvm.base.BaseFragment
import com.androidfood.mvvm.callback.ActionCallback
import com.androidfood.mvvm.data.model.auth.ForgetPassword
import com.androidfood.mvvm.data.remote.GeneralApiResponse
import com.androidfood.mvvm.databinding.ForgotPasswordBinding
import com.androidfood.mvvm.helper.GeneralHelper
import com.androidfood.mvvm.vm.auth.LoginViewModel
import kotlinx.android.synthetic.main.forgot_password.*

class ForgotPassword : BaseFragment(), ActionCallback {

    private lateinit var binding: ForgotPasswordBinding

    val loginViewModel: LoginViewModel by viewModels()

    companion object {
        @JvmStatic
        fun newInstance() = ForgotPassword()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            binding<ForgotPasswordBinding>(inflater, R.layout.forgot_password, container).apply {
                lifecycleOwner = viewLifecycleOwner
                callback = this@ForgotPassword
            }

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backArrow.setOnClickListener {
            replaceWithNewFragment(LoginFragment.newInstance())
        }

        loginViewModel.forgetPasswordState.observe(viewLifecycleOwner, Observer {
            when(it){

                is GeneralApiResponse.Loading -> {
                    binding.loadingView.visibility = View.VISIBLE
                }

                is GeneralApiResponse.Failure -> {
                    binding.loadingView.visibility = View.GONE
                    GeneralHelper.showDialog(requireActivity(), it.error, object : ActionCallback {
                        override fun onBindingClick(view: View) {
                        }

                        override fun onBindingClick(id: Int) {
                        }

                    })
                }

                is GeneralApiResponse.Success<ForgetPassword> -> {
                    binding.loadingView.visibility = View.GONE
                    GeneralHelper.showDialog(requireActivity(), it.data?.message, object : ActionCallback {
                        override fun onBindingClick(view: View) {
                        }

                        override fun onBindingClick(id: Int) {
                            replaceWithNewFragment(OTPVerification.newInstance())
                        }

                    })
                }



            }
        })
    }

    override fun onBindingClick(view: View) {
        when (view.id) {
            R.id.forget_btn -> {
                loginViewModel.userForgetPassword(forgetEmail.editText?.text.toString())
            }
        }
    }

    override fun onBindingClick(id: Int) {
        TODO("Not yet implemented")
    }


    private fun replaceWithNewFragment(fragment: Fragment) {
        replaceFragment(
            fragment,
            R.id.auth_container,
            addBackStack = false,
            clearBackStack = true
        )
    }

}