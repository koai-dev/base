package com.koai.base.core.action.router

import android.net.Uri
import android.os.Bundle

interface BaseRouter {
    fun onNextScreen(
        action: Int,
        extras: Bundle = Bundle(),
    ): Boolean

    fun onPopScreen(
        action: Int? = null,
        inclusive: Boolean? = null,
        saveState: Boolean? = null,
    ): Boolean

    fun onSessionTimeout(
        action: Int = 0,
        extras: Bundle? = null,
    )

    fun onOtherErrorDefault(
        action: Int,
        extras: Bundle? = null,
    )

    fun onShareFile(extras: Bundle?)

    fun gotoComingSoon(
        action: Int,
        extras: Bundle?,
    )

    fun backToHome(
        action: Int,
        extras: Bundle? = null,
    )

    fun openDeeplink(
        uri: Uri? = null,
        extras: Bundle? = null,
    )

    fun notImplemented()

    fun notRecognized()
}
