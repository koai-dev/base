package com.koai

import android.app.Application
import com.koai.base.BaseApplication
import org.koin.core.module.Module

class MyApplication : BaseApplication() {
    override fun appModule(): Module {
        return super.appModule().apply {

        }
    }
}