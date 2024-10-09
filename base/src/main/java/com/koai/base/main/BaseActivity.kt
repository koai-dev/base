package com.koai.base.main

import android.app.PendingIntent
import android.content.Intent
import android.graphics.drawable.Icon
import android.os.Build
import android.os.Bundle
import android.service.chooser.ChooserAction
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.navOptions
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
import com.koai.base.main.action.event.PermissionResultEvent
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
    var networkConnected = false
    var onPermissionResult: ((requestCode: Int, permissions: Array<out String>, grantResults: IntArray, deviceId: Int) -> Unit)? = null

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
            is ShareFile -> onShareFile(event.extras)
            is ComingSoon -> gotoComingSoon(event.action, event.extras)
            is BackToHome -> backToHome(event.action, event.extras)
            is NavigateWithDeeplink -> openDeeplink(event.action, event.extras)
            is NotImplementedYet -> notImplemented()
            is PermissionResultEvent -> onPermissionResult?.invoke(event.requestCode, event.permissions, event.grantResults, event.deviceId)
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
        Toast.makeText(this, "Hello recognized sasdnjas", Toast.LENGTH_SHORT).show()
    }

    override fun onOtherErrorDefault(
        action: Int,
        extras: Bundle?,
    ) {
    }

    override fun onShareFile(extras: Bundle?) {
        val sendIntent =
            Intent(Intent.ACTION_SEND)
                .setType("*/*")
        val contentUri = extras?.getString(ShareFile.EXTRA)?.toUri()
        sendIntent.apply {
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            extras?.getString(ShareFile.LINK)?.let {
                putExtra(Intent.EXTRA_TEXT, extras.getString(ShareFile.LINK))
            }
            contentUri?.let {
                data = contentUri
                putExtra(Intent.EXTRA_STREAM, contentUri)
            }
        }

        val shareIntent =
            Intent.createChooser(sendIntent, extras?.getString(ShareFile.TITLE))

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            val customActions =
                arrayOf(
                    ChooserAction.Builder(
                        Icon.createWithResource(this, R.drawable.baseline_mode_edit_outline_24),
                        "Custom",
                        PendingIntent.getBroadcast(
                            this,
                            1,
                            Intent(Intent.ACTION_VIEW),
                            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_CANCEL_CURRENT,
                        ),
                    ).build(),
                )
            shareIntent.putExtra(Intent.EXTRA_CHOOSER_CUSTOM_ACTIONS, customActions)
        }
        startActivity(shareIntent)
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
            networkConnected = it
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

    fun toggleProgressLoading(
        isShow: Boolean,
        isPreventClicked: Boolean = false,
    ) {
        rootView.hasLoading = isShow
        rootView.preventClicked = isPreventClicked
    }
}
