package com.koai.base.app

import android.app.Application
import com.koai.base.core.action.navigator.BaseNavigator
import com.koai.base.core.viewmodel.BaseViewModel
import com.koai.base.core.worker.sessiontimeout.SessionTimeout
import com.koai.base.core.worker.sessiontimeout.SessionTimeoutImpl
import com.koai.base.utils.EncryptionHelper
import com.koai.base.utils.EncryptionHelperImpl
import com.koai.base.utils.LogUtils
import com.koai.base.utils.SharePreference
import com.koai.base.utils.SharePreferenceImpl
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.core.module.dsl.viewModel
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
            single<EncryptionHelper> { EncryptionHelperImpl() }
            single<SharePreference> { SharePreferenceImpl(get(), get()) }
            single<SessionTimeout> { SessionTimeoutImpl(get()) }
        }
}
