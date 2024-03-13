package com.koai.base.main.action.navigator

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.launch

open class BaseNavigator : ViewModel(), BaseRouter {
    val navigation = Channel<NavigationEvent>(Channel.RENDEZVOUS)

    val receive: ReceiveChannel<NavigationEvent> get() = navigation

    fun offNavScreen(
        action: Int,
        extras: Bundle? = null,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            navigation.trySend(NextScreen(action, extras))
        }
    }

    fun offNavScreen(nextScreen: NextScreen) {
        viewModelScope.launch(Dispatchers.IO) {
            navigation.send(nextScreen)
        }
    }

    fun sendEvent(navigationEvent: NavigationEvent) {
        viewModelScope.launch(Dispatchers.IO) {
            navigation.send(navigationEvent)
        }
    }

    override fun onNextScreen(
        action: Int,
        extras: Bundle?,
    ): Boolean {
        offNavScreen(action, extras)
        return true
    }

    override fun onPopScreen(): Boolean {
        sendEvent(PopScreen())
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

    override fun onShareFile(
        action: Int,
        extras: Bundle?,
    ) {
        sendEvent(ShareFile(action, extras))
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
        action: Int,
        extras: Bundle?,
    ) {
        sendEvent(NavigateWithDeeplink(action, extras))
    }

    override fun notImplemented() {
        sendEvent(NotImplementedYet)
    }

    override fun notRecognized() {
        sendEvent(NavigationEvent())
    }
}
