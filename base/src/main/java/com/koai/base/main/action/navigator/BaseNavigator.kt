package com.koai.base.main.action.navigator

import android.net.Uri
import android.os.Bundle
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
import com.koai.base.main.action.router.BaseRouter
import com.koai.base.main.viewmodel.BaseViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel

open class BaseNavigator :
    BaseViewModel(),
    BaseRouter {
    val navigation = Channel<NavigationEvent>(Channel.RENDEZVOUS)

    val receive: ReceiveChannel<NavigationEvent> get() = navigation

    fun offNavScreen(
        action: Int,
        extras: Bundle = Bundle(),
        isFinished: Boolean = false,
    ) {
        launchCoroutine {
            navigation.send(
                NextScreen(
                    action,
                    extras.apply {
                        putBoolean("isFinished", isFinished)
                    },
                ),
            )
        }
    }

    fun offNavScreen(
        nextScreen: NextScreen,
        isFinished: Boolean = false,
    ) {
        launchCoroutine {
            navigation.send(
                nextScreen.copy(
                    extras =
                        nextScreen.extras.apply {
                            putBoolean("isFinished", isFinished)
                        },
                ),
            )
        }
    }

    fun sendEvent(navigationEvent: NavigationEvent) {
        launchCoroutine {
            navigation.send(navigationEvent)
        }
    }

    override fun onNextScreen(
        action: Int,
        extras: Bundle,
    ): Boolean {
        offNavScreen(action, extras)
        return true
    }

    override fun onPopScreen(
        action: Int?,
        inclusive: Boolean?,
        saveState: Boolean?,
    ): Boolean {
        sendEvent(PopScreen(action = action ?: -1, inclusive = inclusive, saveState = saveState))
        return true
    }

    override fun onSessionTimeout(
        action: Int,
        extras: Bundle?,
    ) {
        sendEvent(SessionTimeout(action, extras))
    }

    override fun onOtherErrorDefault(
        action: Int,
        extras: Bundle?,
    ) {
        sendEvent(OtherError(action, extras))
    }

    override fun onShareFile(extras: Bundle?) {
        sendEvent(ShareFile(extras = extras))
    }

    override fun gotoComingSoon(
        action: Int,
        extras: Bundle?,
    ) {
        sendEvent(ComingSoon(action, extras))
    }

    override fun backToHome(
        action: Int,
        extras: Bundle?,
    ) {
        sendEvent(BackToHome(action, extras))
    }

    override fun openDeeplink(
        uri: Uri?,
        extras: Bundle?,
    ) {
        sendEvent(NavigateWithDeeplink(uri, extras))
    }

    override fun notImplemented() {
        sendEvent(NotImplementedYet)
    }

    override fun notRecognized() {
        sendEvent(NavigationEvent())
    }
}
