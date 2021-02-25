package com.androidfood.mvvm.ui.profile

import android.os.Bundle
import android.view.View
import com.androidfood.mvvm.R
import com.androidfood.mvvm.base.BaseActivity
import com.androidfood.mvvm.callback.ActionCallback
import com.androidfood.mvvm.databinding.ActivityAboutBinding
import com.androidfood.mvvm.databinding.ActivityProfileBinding
import com.androidfood.mvvm.databinding.ActivityProfileSettingBinding
import kotlinx.android.synthetic.main.activity_profile_setting.*
import org.jetbrains.anko.startActivity

class ProfileSetting : BaseActivity(), ActionCallback {


    private val binding: ActivityProfileSettingBinding by binding(R.layout.activity_profile_setting)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding.aboutToolbar.titleTbTxt.text = "Profile Setting"
        binding.aboutToolbar.callback = this

        ProfileUpdate.setOnClickListener {
            startActivity<UpdateProfile>()
        }

        ChangePass.setOnClickListener {
            startActivity<ChangePassoword>()
        }
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