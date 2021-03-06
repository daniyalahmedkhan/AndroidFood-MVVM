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
import com.androidfood.mvvm.data.model.auth.Register.UserRegistrationModel
import com.androidfood.mvvm.data.remote.GeneralApiResponse
import com.androidfood.mvvm.databinding.ResetPasswordBinding
import com.androidfood.mvvm.helper.GeneralHelper
import com.androidfood.mvvm.vm.auth.LoginViewModel

class ChangePasswordFragment : BaseFragment(), ActionCallback {

    private lateinit var binding: ResetPasswordBinding
    val loginViewModel: LoginViewModel by viewModels()


    companion object {
        @JvmStatic
        fun newInstance() = ChangePasswordFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            binding<ResetPasswordBinding>(inflater, R.layout.reset_password, container).apply {
                lifecycleOwner = viewLifecycleOwner
                callback = this@ChangePasswordFragment
            }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backArrow.setOnClickListener {
            replaceWithNewFragment(OTPVerification.newInstance())
        }

        loginViewModel.resetPasswordState.observe(viewLifecycleOwner, Observer {

            when (it) {

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

                is GeneralApiResponse.Success<UserRegistrationModel> -> {
                    binding.loadingView.visibility = View.GONE
                    replaceWithNewFragment(LoginFragment.newInstance())
                }

            }


        })
    }

    override fun onBindingClick(view: View) {
        when (view.id) {
            R.id.Btn_changePass -> {
                loginViewModel.userResetPassword(
                    arrayOf(
                        binding.verificationCode,
                        binding.userEmail,
                        binding.password,
                        binding.confirmationPassword
                    )
                )
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