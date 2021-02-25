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
import com.androidfood.mvvm.constants.AppConstants_Java
import com.androidfood.mvvm.data.model.auth.Register.UserRegistrationModel
import com.androidfood.mvvm.data.remote.GeneralApiResponse
import com.androidfood.mvvm.databinding.FragmentSignupBinding
import com.androidfood.mvvm.helper.GeneralHelper
import com.androidfood.mvvm.helper.PrefsHelper
import com.androidfood.mvvm.ui.home.HomeActivity
import com.androidfood.mvvm.vm.auth.RegistrationViewModel
import org.jetbrains.anko.support.v4.startActivity

class SignupFragment : BaseFragment(), ActionCallback {

    private lateinit var binding: FragmentSignupBinding

    val registrationViewModel: RegistrationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            binding<FragmentSignupBinding>(inflater, R.layout.fragment_signup, container).apply {
                lifecycleOwner = viewLifecycleOwner
                callback = this@SignupFragment
            }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activateHideKeyboardUponTouchingScreen(view)

        registrationViewModel.registrationState.observe(viewLifecycleOwner, Observer {
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
                            // leave empty, reusing the callback
                        }

                    })
                }

                is GeneralApiResponse.Success<UserRegistrationModel> -> {
                    GeneralHelper.updateUserInfo(it.data)
                    PrefsHelper.putBoolean(AppConstants_Java.isLogin, true)
                    startActivity<HomeActivity>()
                }
            }

        })
    }

    companion object {
        @JvmStatic
        fun newInstance() = SignupFragment()
    }

    override fun onBindingClick(view: View) {
        when (view.id) {
            R.id.signup_signup_link -> {
                replaceWithNewFragment(LoginFragment.newInstance())
            }
            R.id.signup_btn -> {
                registrationViewModel.handleUserInput(
                    arrayOf(
                        binding.signupNameIp,
                        binding.signupEmailIp,
                        binding.signupPhoneIp,
                        binding.signupPassIp,
                        binding.signupCunIp,
                        binding.signupAddressIp
                    )
                )
            }
        }
    }

    override fun onBindingClick(id: Int) {

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