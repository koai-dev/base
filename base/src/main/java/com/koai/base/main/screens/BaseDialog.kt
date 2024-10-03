package com.koai.base.main.screens

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.viewbinding.ViewBinding
import com.koai.base.main.BaseActivity
import com.koai.base.main.action.navigator.BaseNavigator
import com.koai.base.main.action.router.BaseRouter

/**
 * Base ui dialog fragment
 */
abstract class BaseDialog<T : ViewBinding, Router : BaseRouter, F : BaseNavigator>(private val layoutId: Int = 0) : DialogFragment() {
    lateinit var binding: T
    lateinit var activity: BaseActivity<*, *, *>
    abstract val navigator: F
    protected var router: Router? = null
    open var gravity: Int = Gravity.CENTER

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = DataBindingUtil.inflate(layoutInflater, layoutId, container, false)
        activity = requireActivity() as BaseActivity<*, *, *>
        return binding.root
    }

    @Suppress("UNCHECKED_CAST")
    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        try {
            (navigator as? Router?)?.let {
                router = it
            }
        } catch (e: Exception) {
            Log.e("Cast router error: ", e.message.toString())
        }
        dialog?.window?.setGravity(gravity)
        initView(savedInstanceState, binding)
    }

    abstract fun initView(
        savedInstanceState: Bundle?,
        binding: T,
    )
}
