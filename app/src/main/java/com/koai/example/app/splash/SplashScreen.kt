package com.koai.example.app.splash

import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.koai.base.main.screens.BaseScreen
import com.koai.example.app.MainNavigator
import com.koai.example.R
import com.koai.example.databinding.ScreenSplashBinding

class SplashScreen : BaseScreen<ScreenSplashBinding, SplashRouter, MainNavigator>(R.layout.screen_splash) {

    override fun initView(savedInstanceState: Bundle?, binding: ScreenSplashBinding) {
        binding.splashImageView.setOnClickListener {
            router?.goToHome()
            Toast.makeText(requireContext(), "goToHome", Toast.LENGTH_SHORT).show()
        }
//        Handler().postDelayed({
//            router?.goToHome()
//        }, 5000)
    }

    override fun getModelNavigator()= ViewModelProvider(activity)[MainNavigator::class.java]

}