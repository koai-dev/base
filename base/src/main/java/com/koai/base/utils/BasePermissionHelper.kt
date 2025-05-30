package com.koai.base.utils

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat

abstract class BasePermissionHelper() {
    protected abstract fun permissions(): Array<String>
    protected abstract fun handleResult(isGranted: Boolean, notGrantedPermissions: Array<String>? = null)

    private val permissionResults = mutableMapOf<String, Boolean>()
    private lateinit var  requestPermissionLauncher: ActivityResultLauncher<Array<String>>

    fun initLauncher(activity: AppCompatActivity){
        requestPermissionLauncher = activity.registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            permissions.forEach { permission ->
                permissionResults[permission.key] = permission.value
            }
            handleResult(permissionResults.all { it.value }, permissionResults.filter { !it.value }.map { it.key }.toTypedArray())
        }
    }

    fun getNotGrantedPermissions(): Array<String> = permissionResults.filter { !it.value }.map { it.key }.toTypedArray()

    open fun hasPermissions(context: Context): Boolean {
        permissionResults.clear()
        permissions().forEach { permission->
            permissionResults[permission] = ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
        }
        return permissionResults.all { it.value }
    }

    open fun requestPermissions(activity: AppCompatActivity) {
        if (!::requestPermissionLauncher.isInitialized) {
            throw IllegalStateException("You must call initLauncher() before using this class.")
        }
        if (hasPermissions(activity)) {
            handleResult(true)
        } else {
            val notGrantedPermissions = permissionResults.filter { !it.value }.map{permissionResult -> permissionResult.key}.toTypedArray()
            requestPermissionLauncher.launch(notGrantedPermissions)
        }
    }

}

sealed class PermissionHelper {
    companion object {
        private const val KEY_APP_PACKAGE_NAME = "app_package"
        private const val KEY_APP_UID = "app_uid"
    }

    abstract class Camera :
        BasePermissionHelper() {
        override fun permissions(): Array<String> = arrayOf(Manifest.permission.CAMERA)
    }

    abstract class ReadContact : BasePermissionHelper() {
        override fun permissions(): Array<String> = arrayOf(Manifest.permission.READ_CONTACTS)
    }

    abstract class CallPhone : BasePermissionHelper() {
        override fun permissions(): Array<String> = arrayOf(Manifest.permission.CALL_PHONE)
    }

    abstract class ReadExternalStorage : BasePermissionHelper() {
        override fun permissions(): Array<String> = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    abstract class WriteExternalStorage : BasePermissionHelper() {
        override fun permissions(): Array<String> = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

    abstract class Location : BasePermissionHelper() {
        override fun permissions(): Array<String> = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    abstract class RecordAudio : BasePermissionHelper() {
        override fun permissions(): Array<String> = arrayOf(Manifest.permission.RECORD_AUDIO)
    }

    abstract class WakeLock : BasePermissionHelper() {
        override fun permissions(): Array<String> = arrayOf(Manifest.permission.WAKE_LOCK)
    }

    abstract class PostNotification : BasePermissionHelper() {
        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        override fun permissions(): Array<String> = arrayOf(Manifest.permission.POST_NOTIFICATIONS)

        override fun hasPermissions(context: Context): Boolean =
            NotificationManagerCompat
                .from(context)
                .areNotificationsEnabled()

        override fun requestPermissions(activity: AppCompatActivity) {
            try {
                val intent = Intent()
                if (!NotificationManagerCompat
                        .from(activity)
                        .areNotificationsEnabled()
                ) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        intent.action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
                        intent.putExtra(Settings.EXTRA_APP_PACKAGE, activity.packageName)
                    } else {
                        intent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
                    }
                }
                intent.putExtra(KEY_APP_PACKAGE_NAME, activity.packageName)
                intent.putExtra(KEY_APP_UID, activity.applicationInfo.uid)
                activity.startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    abstract class AllPermission : BasePermissionHelper() {
        override fun permissions() =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                arrayOf(
                    Manifest.permission.POST_NOTIFICATIONS,
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_CONTACTS,
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CALL_PHONE,
                    Manifest.permission.WAKE_LOCK,
                )
            } else {
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_CONTACTS,
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CALL_PHONE,
                    Manifest.permission.WAKE_LOCK,
                )
            }

        override fun hasPermissions(context: Context): Boolean =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                super.hasPermissions(context) &&
                    NotificationManagerCompat
                        .from(context)
                        .areNotificationsEnabled()
            } else {
                super.hasPermissions(context)
            }

        override fun requestPermissions(activity: AppCompatActivity) {
            super.requestPermissions(activity)
            try {
                val intent = Intent()
                if (!NotificationManagerCompat
                        .from(activity)
                        .areNotificationsEnabled()
                ) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        intent.action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
                        intent.putExtra(Settings.EXTRA_APP_PACKAGE, activity.packageName)
                    } else {
                        intent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
                    }
                }
                intent.putExtra(KEY_APP_PACKAGE_NAME, activity.packageName)
                intent.putExtra(KEY_APP_UID, activity.applicationInfo.uid)
                activity.startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
