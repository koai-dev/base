package com.koai.example.contact

import android.os.Bundle
import com.koai.base.app.SDKInitializer
import com.koai.base.core.action.navigator.BaseNavigator
import com.koai.base.di.navigatorViewModel
import com.koai.base.core.ui.extension.withSafeContext
import com.koai.base.core.ui.screens.BaseJourney
import com.koai.base.utils.PermissionHelper
import com.koai.example.R
import com.koai.example.databinding.JourneyContactBinding


object ContactSDKInit : SDKInitializer {
    lateinit var mainNavigator: BaseNavigator
    override fun init(navigator: BaseNavigator) {
        mainNavigator = navigator
    }

}

class ContactJourney :
    BaseJourney<JourneyContactBinding, ContactRouter, ContactNavigator>(R.layout.journey_contact) {
        private val launchPermissions = object : PermissionHelper.Camera() {
            override fun handleResult(
                isGranted: Boolean,
                notGrantedPermissions: Array<String>?
            ) {
                TODO("Not yet implemented")
            }

        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        withSafeContext {activity ->
            launchPermissions.initLauncher(activity)
        }

    }
    override fun initView(savedInstanceState: Bundle?, binding: JourneyContactBinding) {
        withSafeContext {
            launchPermissions.requestPermissions(it)
        }
    }

    override val navigator: ContactNavigator by navigatorViewModel()
    override val mainNavigator: BaseNavigator = ContactSDKInit.mainNavigator
}