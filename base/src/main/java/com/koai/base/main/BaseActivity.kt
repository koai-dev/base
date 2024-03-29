package com.koai.base.main

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.viewbinding.ViewBinding
import com.koai.base.R
import com.koai.base.databinding.ActivityBaseBinding
import com.koai.base.main.action.event.BackToHome
import com.koai.base.main.action.event.ComingSoon
import com.koai.base.main.action.event.NavigateWithDeeplink
import com.koai.base.main.action.event.NavigationEvent
import com.koai.base.main.action.event.NextScreen
import com.koai.base.main.action.event.NotImplementedYet
import com.koai.base.main.action.event.OtherError
import com.koai.base.main.action.event.PopScreen
import com.koai.base.main.action.event.SessionTimeout
import com.koai.base.main.action.event.ShareFile
import com.koai.base.main.action.navigator.BaseNavigator
import com.koai.base.main.action.router.BaseRouter
import com.koai.base.utils.NetworkUtil
import com.koai.base.widgets.BaseLoadingView
import kotlinx.coroutines.launch

abstract class BaseActivity<T : ViewBinding, Router : BaseRouter, F : BaseNavigator>(private val layoutId: Int) :
    AppCompatActivity(), BaseRouter {
    var navController: NavController? = null
    lateinit var binding: T
    abstract val navigator: F
    protected var router: Router? = null
    private lateinit var rootView: ActivityBaseBinding
    var statusBarHeight = 32
    var bottomNavigationHeight = 32

    @Suppress("UNCHECKED_CAST")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        rootView = DataBindingUtil.inflate(layoutInflater, R.layout.activity_base, null, false)
        binding = DataBindingUtil.inflate(layoutInflater, layoutId, rootView.container, true)
        rootView.loading.addView(getLoadingView())
        setContentView(rootView.root)
        val navHostFragment =
            supportFragmentManager
                .findFragmentById(R.id.container) as NavHostFragment?
        navController = navHostFragment?.navController
        try {
            (navigator as? Router?)?.let {
                router = it
            }
        } catch (e: Exception) {
            throw e
        }

        initView(savedInstanceState, binding)

        checkNetwork()
        onNavigationEvent()
    }

    private fun onNavigationEvent() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
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
            is ShareFile -> onShareFile(event.action, event.extras)
            is ComingSoon -> gotoComingSoon(event.action, event.extras)
            is BackToHome -> backToHome(event.action, event.extras)
            is NavigateWithDeeplink -> openDeeplink(event.action, event.extras)
            is NotImplementedYet -> notImplemented()
            else -> notRecognized()
        }
    }

    override fun onNextScreen(
        action: Int,
        extras: Bundle?,
    ): Boolean {
        try {
            navController?.navigate(action, extras)
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
        Toast.makeText(this, "Hello recognized sasdnjas", Toast.LENGTH_SHORT).show()
    }

    override fun onOtherErrorDefault(
        action: Int,
        extras: Bundle?,
    ) {
    }

    override fun onShareFile(
        action: Int,
        extras: Bundle?,
    ) {
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

    private fun checkNetwork() {
        NetworkUtil(this).observe(this) {
            if (!it) {
                Toast.makeText(
                    this,
                    resources.getString(R.string.you_are_offline),
                    Toast.LENGTH_SHORT,
                ).show()
            }
        }
    }

    abstract fun initView(
        savedInstanceState: Bundle?,
        binding: T,
    )

    open fun getLoadingView(): View {
        return BaseLoadingView(this).apply {
            layoutParams =
                ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.MATCH_PARENT,
                    ConstraintLayout.LayoutParams.MATCH_PARENT,
                )
        }
    }

    fun toggleProgressLoading(isShow: Boolean) {
        rootView.hasLoading = isShow
    }
}
