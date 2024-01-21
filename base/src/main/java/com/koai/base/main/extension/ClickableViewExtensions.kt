package com.koai.base.main.extension

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.SystemClock
import android.view.MotionEvent
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.imageview.ShapeableImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object ClickableViewExtensions{
    private var mLastClickTime = 0L
    @SuppressLint("ClickableViewAccessibility")
    fun ShapeableImageView.setClickableWithScale(onClick: () -> Unit) {
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
    fun ShapeableImageView.loadImage(source: Any) {
        when (source) {
            is Drawable -> loadImageFromDrawable(source)
            is String -> loadImageFromUrl(source)
            is Int -> loadImageFromColor(source)
            else -> throw IllegalArgumentException("Unsupported image source type")
        }
    }

    private fun ShapeableImageView.loadImageFromDrawable(drawable: Drawable) {
        Glide.with(this)
            .load(drawable)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(this)
    }

    private fun ShapeableImageView.loadImageFromUrl(url: String) {
        Glide.with(this)
            .load(url)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(this)
    }

    private fun ShapeableImageView.loadImageFromColor(color: Int) {
        Glide.with(this)
            .load(color)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(this)
    }

}
