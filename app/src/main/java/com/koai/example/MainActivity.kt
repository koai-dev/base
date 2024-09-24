package com.koai.example

import android.os.Bundle
import com.koai.base.main.BaseActivity
import com.koai.base.main.extension.ClickableViewExtensions
import com.koai.example.databinding.ActivityMainBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity :
    BaseActivity<ActivityMainBinding, MainRouter, MainNavigator>(R.layout.activity_main) {

    override fun initView(savedInstanceState: Bundle?, binding: ActivityMainBinding) {
        router?.openSomeDestination(this)
        ClickableViewExtensions.initSoundEffect()
    }

    override val navigator: MainNavigator by viewModel()
}