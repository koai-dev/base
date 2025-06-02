package com.koai.base

import android.app.Application
import com.koai.base.main.action.navigator.BaseNavigator
import com.koai.base.main.viewmodel.BaseViewModel
import com.koai.base.utils.EncryptPreference
import com.koai.base.utils.LogUtils
import com.koai.base.utils.SharePreference
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module

abstract class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            // Log Koin into Android logger
            if (LogUtils.getDebugMode()) {
                androidLogger(Level.DEBUG)
            }
            // Reference Android context
            androidContext(this@BaseApplication)
            // Load modules
            modules(appModule())
            androidFileProperties()
        }
    }

    open fun appModule() =
        module {
            viewModel<BaseNavigator> { BaseNavigator() }
            viewModel { BaseViewModel() }
            factory<SharePreference> { SharePreference(get()) }
            factory { EncryptPreference(get()) }
        }
}
