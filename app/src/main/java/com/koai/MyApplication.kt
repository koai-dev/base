package com.koai

import com.koai.base.BaseApplication
import com.koai.base.main.extension.navigatorViewModel
import com.koai.base.utils.GsonUtils
import com.koai.example.MainNavigator
import com.koai.example.contact.ContactNavigator
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

class MyApplication : BaseApplication() {
    override fun appModule() = module {
        includes(super.appModule())
        viewModel { MainNavigator() }
        navigatorViewModel { ContactNavigator() }
        GsonUtils.fromJson("", User::class.java)
    }
}