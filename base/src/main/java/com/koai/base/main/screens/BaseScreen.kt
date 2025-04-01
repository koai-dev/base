package com.koai.base.main.screens

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewbinding.ViewBinding
import com.koai.base.main.BaseActivity
import com.koai.base.main.action.event.ErrorEvent
import com.koai.base.main.action.navigator.BaseNavigator
import com.koai.base.main.action.router.BaseRouter
import com.koai.base.main.extension.screenViewModel
import com.koai.base.main.viewmodel.BaseViewModel
import com.koai.base.utils.LogUtils
import kotlinx.coroutines.launch

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
    open val viewModel: BaseViewModel by screenViewModel()

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
        if (isSecureScreen()) {
            activity.window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)
        }
        try {
            (navigator as? Router?)?.let {
                router = it
            }
        } catch (e: Exception) {
            Log.e("Cast router error: ", e.message.toString())
        }
        registerObserver()
        registerPermissionListener()
        initView(savedInstanceState, binding)
        setupOnBackPressEvent()
    }

    private fun registerPermissionListener() {
        activity.onPermissionResult = { requestCode, permissions, grantResults, deviceId ->
            onPermissionResult(requestCode, permissions, grantResults, deviceId)
        }
    }

    open fun onPermissionResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
        deviceId: Int,
    ) = Unit

    private fun registerObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                observerError()
                observer()
            }
        }
    }

    /**
     * Register collect flow here
     */
    open suspend fun observer() = Unit

    /**
     * Override default error flow here
     */
    open suspend fun observerError() {
        viewModel.uiErrorState.collect { errorState ->
            if (LogUtils.getDebugMode()) {
                navigator.sendEvent(ErrorEvent(message = errorState.message))
            }
            hideLoading()
        }
    }

    /**
     * Init view here
     * This recall when screen visible
     */
    abstract fun initView(
        savedInstanceState: Bundle?,
        binding: T,
    )

    fun showLoading(preventClicked: Boolean = false) {
        activity.toggleProgressLoading(isShow = true, preventClicked)
    }

    fun hideLoading() {
        activity.toggleProgressLoading(isShow = false, isPreventClicked = false)
    }

    open fun setupOnBackPressEvent() {
        activity.onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (!activity.isPreventClicked) {
                        router?.onPopScreen()
                        hideLoading()
                    }
                }
            },
        )
    }

    override fun onDestroyView() {
        if (isSecureScreen()) {
            activity.window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
        }
        super.onDestroyView()
    }

    override fun onDestroy() {
        viewModel.cancelAll()
        super.onDestroy()
    }

    /**
     * Register screen is secure or not
     * Prevent capture screenshot and record video
     */
    open fun isSecureScreen(): Boolean = false
}
