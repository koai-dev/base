package com.koai.base.main.screens

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.koai.base.R

abstract class BaseBottomSheetDialog<BINDING : ViewBinding> : BottomSheetDialogFragment() {
    protected lateinit var binding: BINDING

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = getBindingView()
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

    abstract fun getBindingView(): BINDING
}
