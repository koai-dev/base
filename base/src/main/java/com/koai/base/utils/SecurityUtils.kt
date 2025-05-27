package com.koai.base.utils

import android.content.Context
import android.os.Build
import com.google.firebase.crashlytics.internal.common.CommonUtils

object SecurityUtils {
    private fun isRunningOnEmulator() =
        Build.FINGERPRINT.contains("generic") ||
            Build.MODEL.contains("Emulator") ||
            Build.MANUFACTURER.contains("Genymotion") ||
            (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic")) ||
            "google_sdk" == Build.PRODUCT

    fun isDeviceSecured(): Boolean {
        if (isRunningOnEmulator()) {
            LogUtils.log("Security", "App is running on an emulator!")
        }

        if (LogUtils.getDebugMode()) {
            LogUtils.log("Security", "App is running in debug mode!")
        }
        return !isRunningOnEmulator() &&
            !LogUtils.getDebugMode() &&
            !CommonUtils.isRooted() &&
            !CommonUtils.isDebuggerAttached() &&
            !CommonUtils.isEmulator()
    }

    private fun isPackageNameTampered(
        context: Context,
        packageName: String,
    ): Boolean = context.packageName != packageName
}
