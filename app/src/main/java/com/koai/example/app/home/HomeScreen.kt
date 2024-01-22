package com.koai.example.app.home

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.koai.base.main.screens.BaseScreen
import com.koai.example.R
import com.koai.example.app.MainNavigator
import com.koai.example.app.login.HomeRouter
import com.koai.example.databinding.ScreenHomeBinding

class HomeScreen : BaseScreen<ScreenHomeBinding, HomeRouter, MainNavigator>(R.layout.screen_home) {
    override fun initView(savedInstanceState: Bundle?, binding: ScreenHomeBinding) {

    }

    override fun getModelNavigator() = ViewModelProvider(activity)[MainNavigator::class.java]

}