package com.koai.base.utils

import android.graphics.drawable.Drawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.imageview.ShapeableImageView

object LoadImageUtil {
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