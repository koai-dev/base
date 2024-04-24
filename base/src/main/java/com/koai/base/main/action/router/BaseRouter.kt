package com.koai.base.main.action.router

import android.os.Bundle

interface BaseRouter {
    fun onNextScreen(
        action: Int,
        extras: Bundle?,
    ): Boolean

    fun onPopScreen(): Boolean

    fun onSessionTimeout(
        action: Int,
        extras: Bundle?,
    )

    fun onOtherErrorDefault(
        action: Int,
        extras: Bundle?,
    )

    fun onShareFile(
        extras: Bundle?,
    )

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
