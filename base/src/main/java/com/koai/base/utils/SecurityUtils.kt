package com.koai.base.utils

import android.os.Build

object SecurityUtils {
    fun isRunningOnEmulator() = Build.FINGERPRINT.contains("generic") ||
            Build.MODEL.contains("Emulator") ||
            Build.MANUFACTURER.contains("Genymotion") ||
            (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic")) ||
            "google_sdk" == Build.PRODUCT

    fun isAppSecured(): Boolean {
        if (isRunningOnEmulator()){
            LogUtils.log("Security", "App is running on an emulator!")
        }

        if (LogUtils.getDebugMode()){
            LogUtils.log("Security", "App is running in debug mode!")
        }
        return !isRunningOnEmulator() && !LogUtils.getDebugMode()
    }
}