@file:Suppress("DEPRECATION")

package com.koai.base.utils

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Parcelable

fun <T : Parcelable> Intent.getSafeParcelableExtra(
    name: String,
    clazz: Class<T>,
): T? =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        this.getParcelableExtra(name, clazz)
    } else {
        this.getParcelableExtra(name)
    }

fun <T : Parcelable> Bundle.getSafeParcelableExtra(
    name: String,
    clazz: Class<T>,
): T? =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        this.getParcelable(name, clazz)
    } else {
        this.getParcelable(name)
    }
