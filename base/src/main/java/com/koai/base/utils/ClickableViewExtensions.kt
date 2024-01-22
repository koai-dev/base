package com.koai.base.utils

import android.annotation.SuppressLint
import android.os.SystemClock
import android.view.MotionEvent
import com.google.android.material.imageview.ShapeableImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object ClickableViewExtensions {
    private var mLastClickTime = 0L

    @SuppressLint("ClickableViewAccessibility")
    fun ShapeableImageView.setClickableWithScale( onClick: () -> Unit) {
        setOnClickListener {
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                return@setOnClickListener
            }
            mLastClickTime = SystemClock.elapsedRealtime()
            onClick.invoke()
        }
        setOnTouchListener { view, event ->
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                    CoroutineScope(Dispatchers.IO).launch {
                        view.scaleX = 1.1f
                        view.scaleY = 1.1f
                        delay(100)
                        view.scaleX = 1f
                        view.scaleY = 1f
                    }
                }
            }
            false
        }
    }
}