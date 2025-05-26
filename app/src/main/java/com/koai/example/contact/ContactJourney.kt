package com.koai.example.contact

import android.os.Bundle
import com.koai.base.main.SDKInitializer
import com.koai.base.main.action.navigator.BaseNavigator
import com.koai.base.main.extension.navigatorViewModel
import com.koai.base.main.screens.BaseJourney
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
    override fun initView(savedInstanceState: Bundle?, binding: JourneyContactBinding) {

    }

    override val navigator: ContactNavigator by navigatorViewModel()
    override val mainNavigator: BaseNavigator = ContactSDKInit.mainNavigator
}