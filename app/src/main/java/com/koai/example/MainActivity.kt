package com.koai.example

import android.os.Bundle
import com.koai.base.app.BaseActivity
import com.koai.base.core.action.event.PermissionResultEvent
import com.koai.base.core.network.BaseApiController
import com.koai.base.core.ui.extension.ClickableViewExtensions
import com.koai.base.utils.SharePreference
import com.koai.example.databinding.ActivityMainBinding
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity :
    BaseActivity<ActivityMainBinding, MainRouter, MainNavigator>(R.layout.activity_main) {
        private val share by inject<SharePreference>()

    override fun initView(savedInstanceState: Bundle?, binding: ActivityMainBinding) {
        router?.openSomeDestination(this)
        ClickableViewExtensions.initSoundEffect()
        binding.root.postDelayed({
            navigator.sendEvent(PermissionResultEvent(0, arrayOf(""), intArrayOf(), 0))
        }, 3000)
        BaseApiController
    }

    override val navigator: MainNavigator by viewModel()
}