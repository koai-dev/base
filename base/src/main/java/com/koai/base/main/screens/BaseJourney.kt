package com.koai.base.main.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.navOptions
import androidx.viewbinding.ViewBinding
import com.koai.base.R
import com.koai.base.main.BaseActivity
import com.koai.base.main.action.event.BackToHome
import com.koai.base.main.action.event.ComingSoon
import com.koai.base.main.action.event.NavigateWithDeeplink
import com.koai.base.main.action.event.NavigationEvent
import com.koai.base.main.action.event.NextScreen
import com.koai.base.main.action.event.NotImplementedYet
import com.koai.base.main.action.event.OtherError
import com.koai.base.main.action.event.PermissionResultEvent
import com.koai.base.main.action.event.PopScreen
import com.koai.base.main.action.event.SessionTimeout
import com.koai.base.main.action.event.ShareFile
import com.koai.base.main.action.navigator.BaseNavigator
import com.koai.base.main.action.router.BaseRouter
import kotlinx.coroutines.launch

/**
 * Base journey screen (base main navigation)
 */
abstract class BaseJourney<T : ViewBinding, Router : BaseRouter, F : BaseNavigator>(
    private val layoutId: Int = 0,
) : Fragment(), BaseRouter {
    lateinit var binding: T
    var navController: NavController? = null
    lateinit var activity: BaseActivity<*, *, *>
    abstract val navigator: F
    protected var router: Router? = null

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
            throw e
        }
        initView(savedInstanceState, binding)
        val navHostFragment =
            childFragmentManager
                .findFragmentById(R.id.container) as NavHostFragment?
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
            is PopScreen -> onPopScreen()
            is SessionTimeout -> onSessionTimeout(event.action, event.extras)
            is OtherError -> onOtherErrorDefault(event.action, event.extras)
            is ShareFile -> onShareFile(event.extras)
            is ComingSoon -> gotoComingSoon(event.action, event.extras)
            is BackToHome -> backToHome(event.action, event.extras)
            is NavigateWithDeeplink -> openDeeplink(event.action, event.extras)
            is NotImplementedYet -> notImplemented()
            is PermissionResultEvent ->
                activity.onPermissionResult?.invoke(
                    event.requestCode,
                    event.permissions,
                    event.grantResults,
                    event.deviceId,
                )
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

    override fun onPopScreen(): Boolean {
        navController?.let { n ->
            n.previousBackStackEntry?.let {
                n.currentBackStackEntry?.let {
                    if (n.popBackStack()) return true
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
        Toast.makeText(activity, "Hello recognized sasdnjas", Toast.LENGTH_SHORT).show()
    }

    override fun onOtherErrorDefault(
        action: Int,
        extras: Bundle?,
    ) {
    }

    override fun onShareFile(extras: Bundle?) {
    }

    override fun gotoComingSoon(
        action: Int,
        extras: Bundle?,
    ) {
    }

    override fun backToHome(
        action: Int,
        extras: Bundle?,
    ) {
    }

    override fun openDeeplink(
        action: Int,
        extras: Bundle?,
    ) {
    }

    override fun notImplemented() {
    }

    override fun notRecognized() {
    }

    abstract fun initView(
        savedInstanceState: Bundle?,
        binding: T,
    )
}
