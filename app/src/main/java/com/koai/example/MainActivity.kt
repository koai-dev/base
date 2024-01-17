package com.koai.example

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.koai.base.main.BaseActivity
import com.koai.base.main.action.navigator.BaseNavigator
import com.koai.base.main.action.router.BaseRouter
import com.koai.example.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding, BaseRouter, BaseNavigator>(R.layout.activity_main) {

    override fun initView(savedInstanceState: Bundle?, binding: ActivityMainBinding) {

    }

    override fun getModelNavigator(): BaseNavigator = ViewModelProvider(this)[BaseNavigator::class.java]
}