package com.koai.base.main.screens

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.imageview.ShapeableImageView
import com.koai.base.main.BaseActivity
import com.koai.base.main.action.navigator.BaseNavigator
import com.koai.base.main.action.router.BaseRouter

/**
 * Base ui screen
 */
abstract class BaseScreen<T: ViewBinding, Router: BaseRouter, F: BaseNavigator>(private val layoutId: Int = 0) : Fragment() {
    lateinit var binding: T
    lateinit var activity: BaseActivity<*,*,*>
    protected var navigator: F? = null
    protected var router: Router? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(layoutInflater, layoutId, container, false)
        activity = requireActivity() as BaseActivity<*,*,*>
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigator = getModelNavigator()
        try {
            router = navigator?.router as Router?
        }catch (e: Exception){
            Log.e("Cast router error: ", e.message.toString())
        }
        initView(savedInstanceState, binding)
    }

    abstract fun initView(savedInstanceState: Bundle?, binding: T)
    abstract fun getModelNavigator(): F?
    @SuppressLint("ClickableViewAccessibility")
    fun ShapeableImageView.setClickableWithScale( onClick: () -> Unit) {
        setOnClickListener {
            onClick.invoke()
        }
        setOnTouchListener { view, event ->
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                    view.scaleX = 1.1f
                    view.scaleY = 1.1f
                    Handler(Looper.getMainLooper()).postDelayed({
                        view.scaleX = 1f
                        view.scaleY = 1f
                    }, 100)
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