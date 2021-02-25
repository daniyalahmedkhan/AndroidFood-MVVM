package com.androidfood.mvvm.ui.profile


import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.androidfood.mvvm.R
import com.androidfood.mvvm.base.BaseActivity
import com.androidfood.mvvm.callback.ActionCallback
import com.androidfood.mvvm.data.model.UpdateProfile
import com.androidfood.mvvm.data.remote.GeneralApiResponse
import com.androidfood.mvvm.databinding.ActivityUpdateProfileBinding
import com.androidfood.mvvm.helper.GeneralHelper
import com.androidfood.mvvm.vm.profile.ProfileViewModel

class UpdateProfile : BaseActivity(), ActionCallback {

    val binding: ActivityUpdateProfileBinding by binding(R.layout.activity_update_profile)
    val profileViewModel: ProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding.callback = this
        binding.aboutToolbar.callback = this

        binding.aboutToolbar.titleTbTxt.text = "Update Profile"
        binding.name.editText?.setText(GeneralHelper.getUsersData().data.user.name)
        binding.phone.editText?.setText(GeneralHelper.getUsersData().data.user.details.phone)
        binding.address.editText?.setText(GeneralHelper.getUsersData().data.user.details.address)

        profileViewModel.profileUpdateState.observe(this, Observer {

            when (it) {

                is GeneralApiResponse.Loading -> {
                    binding.loadingView.visibility = View.VISIBLE
                }

                is GeneralApiResponse.Failure -> {
                    binding.loadingView.visibility = View.GONE
                    GeneralHelper.showDialog(this, it.error, object : ActionCallback {
                        override fun onBindingClick(view: View) {
                        }

                        override fun onBindingClick(id: Int) {
                            // leave empty, reusing the callback
                        }

                    })

                }

                is GeneralApiResponse.Success<UpdateProfile> -> {
                    binding.loadingView.visibility = View.GONE
                    val userRegistrationModel = GeneralHelper.getUsersData()
                    userRegistrationModel.data.user.name = it.data?.data?.firstName
                    userRegistrationModel.data.user.details.firstName =
                        it.data?.data?.firstName
                    userRegistrationModel.data.user.details.phone =
                        it.data?.data?.phone
                    userRegistrationModel.data.user.details.address =
                        it.data?.data?.address
                    GeneralHelper.updateUserInfo(userRegistrationModel)
                    finish()
                }

            }
        })

    }

    override fun onBindingClick(view: View) {
        when (view.id) {
            R.id.updateProfile -> {
                profileViewModel.updateProfile(
                    arrayOf(
                        binding.name,
                        binding.phone,
                        binding.address
                    )
                )
            }

            R.id.title_tb_arrow -> {
                finish()
            }

        }
    }

    override fun onBindingClick(id: Int) {
        TODO("Not yet implemented")
    }
}