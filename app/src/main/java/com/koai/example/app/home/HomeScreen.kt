package com.koai.example.app.home

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.imageview.ShapeableImageView
import com.koai.base.main.screens.BaseScreen
import com.koai.example.app.MainNavigator
import com.koai.example.R
import com.koai.example.databinding.ScreenHomeBinding


class HomeScreen : BaseScreen<ScreenHomeBinding, HomeRouter, MainNavigator>(R.layout.screen_home) {
    override fun initView(savedInstanceState: Bundle?, binding: ScreenHomeBinding) {
        val shapeableImageView : ShapeableImageView = binding.homeImageView
        shapeableImageView.setClickableWithScale {
            //shapeableImageView.loadImage(ResourcesCompat.getDrawable(resources, (R.drawable.ic_launcher_background), null)!!)
            shapeableImageView.loadImage("https://upload.wikimedia.org/wikipedia/commons/thumb/1/1f/Logo_Vua_ti%E1%BA%BFng_Vi%E1%BB%87t.svg/1200px-Logo_Vua_ti%E1%BA%BFng_Vi%E1%BB%87t.svg.png")
        }
    }

    override fun getModelNavigator()= ViewModelProvider(activity)[MainNavigator::class.java]

}