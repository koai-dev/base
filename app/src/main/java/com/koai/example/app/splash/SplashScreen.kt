package com.koai.example.app.splash

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.koai.base.main.screens.BaseScreen
import com.koai.example.app.MainNavigator
import com.koai.example.R
import com.koai.example.databinding.ScreenSplashBinding

class SplashScreen : BaseScreen<ScreenSplashBinding, SplashRouter, MainNavigator>(R.layout.screen_splash) {

    override fun initView(savedInstanceState: Bundle?, binding: ScreenSplashBinding) {
        Handler(Looper.getMainLooper()).postDelayed({
            findNavController().navigate(R.id.action_splashScreen_to_homeScreen)
        }, 3000)
    }

    override fun getModelNavigator()= ViewModelProvider(activity)[MainNavigator::class.java]

}