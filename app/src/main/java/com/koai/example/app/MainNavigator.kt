package com.koai.example.app

import com.koai.base.main.action.navigator.BaseNavigator
import com.koai.example.app.home.HomeRouter
import com.koai.example.app.splash.SplashRouter
import com.koai.example.R

class MainNavigator : BaseNavigator(), MainRouter, SplashRouter, HomeRouter {

    override fun goToPlay() {
        offNavScreen(R.id.action_splashScreen)
    }

    override fun goToHome() {
        offNavScreen(R.id.action_homeScreen)
    }

}