package com.koai.base.core.ui.screens

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.activity.OnBackPressedCallback
import androidx.core.app.ActivityCompat
import androidx.core.bundle.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewbinding.ViewBinding
import com.koai.base.app.BaseActivity
import com.koai.base.core.action.navigator.BaseNavigator
import com.koai.base.core.action.router.BaseRouter
import com.koai.base.core.ui.extension.withSafeContext
import com.koai.base.core.viewmodel.BaseViewModel
import com.koai.base.di.screenViewModel
import com.koai.base.utils.Constants
import com.koai.base.utils.ErrorCode
import kotlinx.coroutines.launch

/**
 * Base ui screen
 */
abstract class BaseScreen<T : ViewBinding, Router : BaseRouter, out F : BaseNavigator>(
    private val layoutId: Int = 0,
) : Fragment(),
    ActivityCompat.OnRequestPermissionsResultCallback {
    private var _binding: T? = null
    protected val binding: T
        get() =
            _binding
                ?: throw IllegalStateException("Binding is null in ${this::class.java.simpleName}")
    abstract val navigator: F
    protected var router: Router? = null
    open val viewModel: BaseViewModel by screenViewModel()

    @Suppress("UNCHECKED_CAST")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            (navigator as? Router?)?.let {
                router = it
            }
        } catch (e: Exception) {
            Log.e("Cast router error: ", e.message.toString())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = DataBindingUtil.inflate(layoutInflater, layoutId, container, false)
        _binding ?: router?.onOtherErrorDefault(
            ErrorCode.ERROR_BINDING_NULL,
            bundleOf(Constants.ERROR_MESSAGE to "Binding is null in ${this::class.java.simpleName}"),
        )
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        withSafeContext { activity ->
            if (isSecureScreen()) {
                activity.window.setFlags(
                    WindowManager.LayoutParams.FLAG_SECURE,
                    WindowManager.LayoutParams.FLAG_SECURE,
                )
            }
            registerObserver()
            initView(savedInstanceState, binding)
            setupOnBackPressEvent(activity)
        }
    }

    open fun onPermissionResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
        deviceId: Int,
    ) = Unit

    private fun registerObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
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
            navigator.onOtherErrorDefault(
                errorState.code ?: ErrorCode.ERROR_DEFAULT,
                bundleOf(Constants.ERROR_MESSAGE to errorState.message),
            )
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
        withSafeContext { activity ->
            activity.toggleProgressLoading(isShow = true, preventClicked)
        }
    }

    fun hideLoading() {
        withSafeContext { activity ->
            activity.toggleProgressLoading(isShow = false, isPreventClicked = false)
        }
    }

    open fun setupOnBackPressEvent(activity: BaseActivity<*, *, *>) {
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
        withSafeContext { activity ->
            if (isSecureScreen()) {
                activity.window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
            }
        }
        _binding = null
        super.onDestroyView()
    }

    override fun onDestroy() {
        viewModel.cancelAll()
        super.onDestroy()
        router = null
    }

    /**
     * Register screen is secure or not
     * Prevent capture screenshot and record video
     */
    open fun isSecureScreen(): Boolean = false
}
