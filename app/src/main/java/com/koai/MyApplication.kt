package com.koai

import android.app.Application
import com.koai.base.BaseApplication
import com.koai.example.MainNavigator
import com.koai.example.contact.ContactNavigator
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module

class MyApplication : BaseApplication() {
    override fun appModule(): Module {
        return super.appModule().apply {
            viewModel { MainNavigator() }
            viewModel { ContactNavigator() }
        }
    }
}