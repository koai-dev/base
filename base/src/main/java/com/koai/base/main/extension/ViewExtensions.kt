package com.koai.base.main.extension

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
