package com.androidfood.mvvm.ui.splash

import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import com.androidfood.mvvm.R
import com.androidfood.mvvm.constants.AppConstants_Java
import com.androidfood.mvvm.helper.PrefsHelper
import com.androidfood.mvvm.ui.auth.AuthActivity
import com.androidfood.mvvm.ui.home.HomeActivity
import kotlinx.android.synthetic.main.activity_splash.*
import org.jetbrains.anko.startActivity

class SplashActivity : AppCompatActivity() {

    private var progressTxt = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        splash_progress.text = "$progressTxt%"
        splash_pb.progress = progressTxt
        timer.start()
    }

    private val timer = object : CountDownTimer(5000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            progressTxt += 20
            splash_progress.text = "${progressTxt}%"
            splash_pb.progress = progressTxt
        }

        override fun onFinish() {
            if (PrefsHelper.getBoolean(AppConstants_Java.isLogin , false)){
                startActivity<HomeActivity>()
                finish()
            }else{
                startActivity<AuthActivity>()
                finish()
            }

        }
    }


}