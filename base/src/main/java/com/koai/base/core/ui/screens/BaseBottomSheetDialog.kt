package com.koai.base.core.ui.screens

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.koai.base.R
import com.koai.base.core.action.navigator.BaseNavigator
import com.koai.base.core.action.router.BaseRouter
import com.koai.base.utils.Constants
import com.koai.base.utils.ErrorCode
import com.koai.base.utils.LogUtils

abstract class BaseBottomSheetDialog<T : ViewBinding, Router : BaseRouter, F : BaseNavigator>(
    private val layoutId: Int = 0,
) : BottomSheetDialogFragment() {
    private var _binding: T? = null
    protected val binding: T
        get() =
            _binding
                ?: throw IllegalStateException("Binding is null in ${this::class.java.simpleName}")
    abstract val navigator: F
    protected var router: Router? = null

    @Suppress("UNCHECKED_CAST")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
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
    ): View {
        _binding = DataBindingUtil.inflate(layoutInflater, layoutId, container, false)
        _binding ?: router?.onOtherErrorDefault(
            ErrorCode.ERROR_BINDING_NULL,
            bundleOf(Constants.ERROR_MESSAGE to "Binding is null in ${this::class.java.simpleName}"),
        )
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheet =
            dialog?.findViewById<FrameLayout?>(com.google.android.material.R.id.design_bottom_sheet)
        val behavior = bottomSheet?.let { BottomSheetBehavior.from(it) }
        behavior?.skipCollapsed = true
        behavior?.state = BottomSheetBehavior.STATE_EXPANDED
        return super.onCreateDialog(savedInstanceState)
    }

    @Deprecated("Don't override this method. Handle your logic in initView() instead!")
    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        initView(savedInstanceState, binding)
    }

    abstract fun initView(
        savedInstanceState: Bundle?,
        binding: T,
    )

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        router = null
    }
}
