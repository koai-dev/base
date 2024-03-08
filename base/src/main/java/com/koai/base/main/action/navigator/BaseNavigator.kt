package com.koai.base.main.action.navigator

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.koai.base.main.action.event.NavigationEvent
import com.koai.base.main.action.event.NextScreen
import com.koai.base.main.action.router.BaseRouter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.launch

open class BaseNavigator : ViewModel(), BaseRouter {
    var router: BaseRouter? = null
    val navigation = Channel<NavigationEvent>(Channel.RENDEZVOUS)

    val receive: ReceiveChannel<NavigationEvent> get() = navigation

    init {
        router = this
    }

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
        TODO("Not yet implemented")
    }

    override fun onSessionTimeout(
        action: Int,
        extras: Bundle?,
    ) {
        TODO("Not yet implemented")
    }

    override fun onOtherErrorDefault(
        action: Int,
        extras: Bundle?,
    ) {
        TODO("Not yet implemented")
    }

    override fun onShareFile(
        action: Int,
        extras: Bundle?,
    ) {
        TODO("Not yet implemented")
    }

    override fun gotoComingSoon(
        action: Int,
        extras: Bundle?,
    ) {
        TODO("Not yet implemented")
    }

    override fun backToHome(
        action: Int,
        extras: Bundle?,
    ) {
        TODO("Not yet implemented")
    }

    override fun openDeeplink(
        extras: Bundle?,
        context: Context,
    ) {
        TODO("Not yet implemented")
    }

    override fun notImplemented() {
        TODO("Not yet implemented")
    }

    override fun notRecognized() {
        TODO("Not yet implemented")
    }
}
