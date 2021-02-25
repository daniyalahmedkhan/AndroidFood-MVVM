package com.androidfood.mvvm.ui.home

import android.os.Bundle
import android.view.View
import com.androidfood.mvvm.R
import com.androidfood.mvvm.base.BaseActivity
import com.androidfood.mvvm.callback.ActionCallback
import com.androidfood.mvvm.databinding.ActivityAboutBinding

class AboutActivity : BaseActivity(), ActionCallback {

    private val binding: ActivityAboutBinding by binding(R.layout.activity_about)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.aboutToolbar.titleTbTxt.text = "About Us"
        binding.aboutToolbar.callback = this
    }

    override fun onBindingClick(view: View) {
        when (view.id) {
            R.id.title_tb_arrow -> {
                finish()
            }
        }
    }

    override fun onBindingClick(id: Int) {

    }
}