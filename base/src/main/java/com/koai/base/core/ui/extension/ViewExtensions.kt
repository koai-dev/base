@file:Suppress("DEPRECATION")

package com.koai.base.core.ui.extension

import android.content.Intent
import android.os.Build
import android.os.Parcelable
import android.os.SystemClock
import android.view.View

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

/**
 * This function avoid double click -> Avoid user rapid click button.
 */
fun View.safeClick(
    debounceTime: Long = 800L,
    action: () -> Unit,
) {
    this.setOnClickListener(
        object : View.OnClickListener {
            private var lastClickTime: Long = 0

            override fun onClick(v: View) {
                if (SystemClock.elapsedRealtime() - lastClickTime < debounceTime) {
                    return
                } else {
                    action()
                }
                lastClickTime = SystemClock.elapsedRealtime()
            }
        },
    )
}

fun <T : Parcelable> Intent.getSafeParcelableExtra(
    name: String,
    clazz: Class<T>,
): T? =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        this.getParcelableExtra(name, clazz)
    } else {
        this.getParcelableExtra(name)
    }
