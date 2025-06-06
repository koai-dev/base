package com.koai

import com.koai.base.app.BaseApplication
import com.koai.base.di.navigatorViewModel
import com.koai.base.utils.GsonUtils
import com.koai.example.MainNavigator
import com.koai.example.contact.ContactNavigator
import com.koai.example.contact.ContactSDKInit
import org.koin.android.ext.android.get
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

class MyApplication : BaseApplication() {
    override fun appModule() = module {
        includes(super.appModule())
        viewModel<MainNavigator> { MainNavigator() }
        navigatorViewModel { ContactNavigator() }
        GsonUtils.fromJson("", User::class.java)
    }

    override fun onCreate() {
        super.onCreate()
        ContactSDKInit.init(get<MainNavigator>())
    }
}