package com.koai.base.core.action.router

import android.net.Uri
import android.os.Bundle

interface BaseRouter {
    /**
     * Handle navigate to next screen
     */
    fun onNextScreen(
        action: Int,
        extras: Bundle = Bundle(),
    ): Boolean

    /**
     * Handle pop screen
     */
    fun onPopScreen(
        action: Int? = null,
        inclusive: Boolean? = null,
        saveState: Boolean? = null,
    ): Boolean

    /**
     * Handle session timeout -> sign out and goto login screen
     */
    fun onSessionTimeout(
        action: Int = 0,
        extras: Bundle? = null,
    )

    /**
     * Handle other error
     * @param action: error code
     * @param extras: error message
     */
    fun onOtherErrorDefault(
        action: Int,
        extras: Bundle? = null,
    )

    /**
     * Handle share file
     */
    fun onShareFile(extras: Bundle?)

    /**
     * Handle goto coming soon, for new feature not implemented yet
     */
    fun gotoComingSoon(
        action: Int,
        extras: Bundle?,
    )

    /**
     * Handle back to home screen
     */
    fun backToHome(
        action: Int,
        extras: Bundle? = null,
    )

    /**
     * Handle open deeplink
     */
    fun openDeeplink(
        uri: Uri? = null,
        extras: Bundle? = null,
    )

    fun notImplemented()

    fun notRecognized()
}
