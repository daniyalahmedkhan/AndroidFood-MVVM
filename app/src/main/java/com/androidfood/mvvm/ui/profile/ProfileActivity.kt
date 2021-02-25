package com.androidfood.mvvm.ui.profile

import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.androidfood.mvvm.R
import com.androidfood.mvvm.base.BaseActivity
import com.androidfood.mvvm.callback.ActionCallback
import com.androidfood.mvvm.databinding.ActivityProfileBinding
import com.androidfood.mvvm.helper.GeneralHelper
import org.jetbrains.anko.startActivity

class ProfileActivity : BaseActivity(), ActionCallback {

    private val binding: ActivityProfileBinding by binding(R.layout.activity_profile)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.callback = this
        binding.aboutToolbar.callback = this
        binding.aboutToolbar.titleTbTxt.text = "Profile"
        binding.edit.setOnClickListener {
            startActivity<UpdateProfile>()
        }
    }

    override fun onResumeFragments() {
        super.onResumeFragments()

        binding.profileName.text = GeneralHelper.getUsersData().data.user.name
        binding.profileEmail.text = GeneralHelper.getUsersData().data.user.email
        binding.profileAddress.text = GeneralHelper.getUsersData().data.user.details.address
        binding.profileNumber.text = GeneralHelper.getUsersData().data.user.details.phone
        binding.profileCreated.text = GeneralHelper.getUsersData().data.user.createdAt

        Glide.with(this)
            .load(GeneralHelper.getUsersData().data.user.details.imageUrl)
            .centerCrop() //4
            .placeholder(R.drawable.app_logo)
            .error(R.drawable.bg_primary)
            .into(binding.profileImage)

    }

    override fun onBindingClick(view: View) {
        when (view.id) {
            R.id.title_tb_arrow -> {
                finish()
            }
        }

    }

    override fun onBindingClick(id: Int) {
        TODO("Not yet implemented")
    }
}