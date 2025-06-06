package com.koai.base.core.ui.screens

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.core.graphics.drawable.toDrawable
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.viewbinding.ViewBinding
import com.koai.base.core.action.navigator.BaseNavigator
import com.koai.base.core.action.router.BaseRouter
import com.koai.base.utils.Constants
import com.koai.base.utils.ErrorCode
import com.koai.base.utils.LogUtils

/**
 * Base ui dialog fragment
 */
abstract class BaseDialog<T : ViewBinding, Router : BaseRouter, F : BaseNavigator>(
    private val layoutId: Int = 0,
) : DialogFragment() {
    private var mBinding: T? = null
    protected val binding: T get() = mBinding ?: throw IllegalStateException("Binding is null in ${this::class.java.simpleName}")
    abstract val navigator: F
    protected var router: Router? = null
    open var gravity: Int = Gravity.CENTER
    open var canceledOnTouchOutside: Boolean = false

    @Suppress("UNCHECKED_CAST")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        super.onCreateDialog(savedInstanceState).apply {
            this.requestWindowFeature(Window.FEATURE_NO_TITLE)
            this.window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
            this.setCanceledOnTouchOutside(canceledOnTouchOutside)

            try {
                (navigator as? Router)?.let {
                    router = it
                }
            } catch (e: Exception) {
                LogUtils.log("Cast router error: ", e.message.toString())
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        mBinding = DataBindingUtil.inflate(layoutInflater, layoutId, container, false)
        mBinding ?: router?.onOtherErrorDefault(
            ErrorCode.ERROR_BINDING_NULL,
            bundleOf(Constants.ERROR_MESSAGE to "Binding is null in ${this::class.java.simpleName}"),
        )
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        this@BaseDialog.dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
        )
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setGravity(gravity)
        initView(savedInstanceState, binding)
    }

    abstract fun initView(
        savedInstanceState: Bundle?,
        binding: T,
    )

    override fun onDestroyView() {
        super.onDestroyView()
        mBinding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        router = null
    }
}
