package com.koai.example

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.koai.base.main.BaseActivity
import com.koai.example.databinding.ActivityMainBinding

class MainActivity :
    BaseActivity<ActivityMainBinding, MainRouter, MainNavigator>(R.layout.activity_main) {

    override fun initView(savedInstanceState: Bundle?, binding: ActivityMainBinding) {
    }

    override fun getModelNavigator(): MainNavigator =
        ViewModelProvider(this)[MainNavigator::class.java]
}