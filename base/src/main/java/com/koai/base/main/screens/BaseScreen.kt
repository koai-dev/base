package com.koai.base.main.screens

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.koai.base.main.BaseActivity
import com.koai.base.main.action.event.ErrorEvent
import com.koai.base.main.action.navigator.BaseNavigator
import com.koai.base.main.action.router.BaseRouter
import com.koai.base.main.viewmodel.BaseViewModel

/**
 * Base ui screen
 */
abstract class BaseScreen<T : ViewBinding, Router : BaseRouter, out F : BaseNavigator>(
    private val layoutId: Int = 0,
) :
    Fragment() {
    protected lateinit var binding: T
    protected lateinit var activity: BaseActivity<*, *, *>
    abstract val navigator: F
    protected var router: Router? = null
    protected var viewModel: BaseViewModel? = null

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
        initView(savedInstanceState, binding)
        observerError()
    }

    private fun observerError() {
        viewModel?.msgException?.observe(this) { message ->
            navigator.sendEvent(ErrorEvent(message = message))
        }
    }

    abstract fun initView(
        savedInstanceState: Bundle?,
        binding: T,
    )

    fun showLoading() {
        activity.toggleProgressLoading(isShow = true)
    }

    fun hideLoading() {
        activity.toggleProgressLoading(isShow = false)
    }
}
