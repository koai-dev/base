package com.koai.base.main.action.router

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
        action: Int,
        extras: Bundle?,
    )

    fun onOtherErrorDefault(
        action: Int,
        extras: Bundle?,
    )

    fun onShareFile(extras: Bundle?)

    fun gotoComingSoon(
        action: Int,
        extras: Bundle?,
    )

    fun backToHome(
        action: Int,
        extras: Bundle?,
    )

    fun openDeeplink(
        action: Int,
        extras: Bundle?,
    )

    fun notImplemented()

    fun notRecognized()
}
