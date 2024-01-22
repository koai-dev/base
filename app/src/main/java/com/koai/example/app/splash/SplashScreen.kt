package com.koai.example.app.splash

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.koai.base.main.screens.BaseScreen
import com.koai.base.utils.ClickableViewExtensions.setClickableWithScale
import com.koai.example.app.MainNavigator
import com.koai.example.R
import com.koai.example.databinding.ScreenSplashBinding

class SplashScreen : BaseScreen<ScreenSplashBinding, SplashRouter, MainNavigator>(R.layout.screen_splash) {

    override fun initView(savedInstanceState: Bundle?, binding: ScreenSplashBinding) {
        Handler(Looper.getMainLooper()).postDelayed({
            binding.shapeableImageView.translationY = -200f
            binding.shapeableImageView.scaleX = 1f
            binding.shapeableImageView.scaleY = 1f
            binding.shapeableButton.visibility = View.VISIBLE
        }, 2000)
        binding.shapeableButton.setClickableWithScale {
            findNavController().navigate(R.id.action_splashScreen_to_loginScreen)
        }
    }

    override fun getModelNavigator()= ViewModelProvider(activity)[MainNavigator::class.java]

}