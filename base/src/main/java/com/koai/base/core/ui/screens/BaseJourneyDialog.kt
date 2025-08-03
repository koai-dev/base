package com.koai.base.core.ui.screens

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.navOptions
import androidx.viewbinding.ViewBinding
import com.koai.base.R
import com.koai.base.core.action.event.BackToHome
import com.koai.base.core.action.event.ComingSoon
import com.koai.base.core.action.event.NavigateWithDeeplink
import com.koai.base.core.action.event.NavigationEvent
import com.koai.base.core.action.event.NextScreen
import com.koai.base.core.action.event.NotImplementedYet
import com.koai.base.core.action.event.OtherError
import com.koai.base.core.action.event.PopScreen
import com.koai.base.core.action.event.SessionTimeout
import com.koai.base.core.action.event.ShareFile
import com.koai.base.core.action.navigator.BaseNavigator
import com.koai.base.core.action.router.BaseRouter
import com.koai.base.utils.Constants
import com.koai.base.utils.ErrorCode
import com.koai.base.utils.LogUtils
import kotlinx.coroutines.launch

/**
 * Base journey dialog (base main navigation), for showcase tutorial
 */
abstract class BaseJourneyDialog<T : ViewBinding, Router : BaseRouter, F : BaseNavigator>(
    private val layoutId: Int = 0,
) : DialogFragment(),
    BaseRouter {
    private var mBinding: T? = null
    protected val binding: T get() = mBinding ?: throw IllegalStateException("Binding is null in ${this::class.java.simpleName}")
    private var navController: NavController? = null
    abstract val navigator: F
    abstract val mainNavigator: BaseNavigator
    protected var router: Router? = null

    @Suppress("UNCHECKED_CAST")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            (navigator as? Router?)?.let {
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

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        initView(savedInstanceState, binding)
        val navHostFragment =
            (
                childFragmentManager
                    .findFragmentById(R.id.container) as NavHostFragment?
            )
                ?: (childFragmentManager.findFragmentByTag(this::class.java.simpleName) as? NavHostFragment)
        navController = navHostFragment?.navController
        onNavigationEvent()
    }

    private fun onNavigationEvent() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                for (event in navigator.receive) {
                    onNavigationEvent(event)
                }
            }
        }
    }

    open fun onNavigationEvent(event: NavigationEvent) {
        when (event) {
            is NextScreen -> onNextScreen(event.action, event.extras)
            is PopScreen ->
                onPopScreen(
                    action = if (event.action != -1) event.action else null,
                    inclusive = event.inclusive,
                    saveState = event.saveState,
                )

            is SessionTimeout -> onSessionTimeout(event.action, event.extras)
            is OtherError -> onOtherErrorDefault(event.action, event.extras)
            is ShareFile -> onShareFile(event.extras)
            is ComingSoon -> gotoComingSoon(event.action, event.extras)
            is BackToHome -> backToHome(event.action, event.extras)
            is NavigateWithDeeplink -> openDeeplink(event.uri, event.extras)
            is NotImplementedYet -> notImplemented()
            else -> notRecognized()
        }
    }

    override fun onNextScreen(
        action: Int,
        extras: Bundle,
    ): Boolean {
        try {
            navController?.navigate(
                resId = action,
                args = extras,
                navOptions =
                    navOptions {
                        if (extras.getBoolean("isFinished", false)) {
                            launchSingleTop = true
                            popUpTo(resources.getString(R.string.app_name)) {
                                inclusive = true
                                saveState = true
                            }
                        }
                    },
            )
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    override fun onPopScreen(
        action: Int?,
        inclusive: Boolean?,
        saveState: Boolean?,
    ): Boolean {
        navController?.let { n ->
            n.previousBackStackEntry?.let {
                n.currentBackStackEntry?.let {
                    action?.let { action ->
                        if (n.popBackStack(
                                destinationId = action,
                                inclusive = inclusive != false,
                                saveState = saveState == true,
                            )
                        ) {
                            return true
                        }
                    } ?: run {
                        if (n.popBackStack()) return true
                    }
                    n.previousBackStackEntry
                    n.navigate(it.destination.id) // reload here
                }
            }
        }
        return false
    }

    override fun onSessionTimeout(
        action: Int,
        extras: Bundle?,
    ) {
        mainNavigator.onSessionTimeout(action, extras)
    }

    override fun onOtherErrorDefault(
        action: Int,
        extras: Bundle?,
    ) {
        mainNavigator.onOtherErrorDefault(action, extras)
    }

    override fun onShareFile(extras: Bundle?) {
        mainNavigator.onShareFile(extras = extras)
    }

    override fun gotoComingSoon(
        action: Int,
        extras: Bundle?,
    ) {
        mainNavigator.gotoComingSoon(action, extras = extras)
    }

    override fun backToHome(
        action: Int,
        extras: Bundle?,
    ) {
        mainNavigator.backToHome(action, extras)
    }

    override fun openDeeplink(
        uri: Uri?,
        extras: Bundle?,
    ) {
        mainNavigator.openDeeplink(uri, extras)
    }

    override fun notImplemented() {
        mainNavigator.notImplemented()
    }

    override fun notRecognized() {
        mainNavigator.notRecognized()
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
