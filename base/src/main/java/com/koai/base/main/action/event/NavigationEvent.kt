package com.koai.base.main.action.event

import android.os.Bundle

open class NavigationEvent {
    open val action: Int = 0
    open val extras: Bundle? = null
}

data class NextScreen(override val action: Int, override val extras: Bundle = Bundle()) :
    NavigationEvent()

data class PopScreen(override val extras: Bundle? = null) : NavigationEvent()

data class JourneyFinished(override val action: Int, override val extras: Bundle? = null) :
    NavigationEvent()

object NotImplementedYet : NavigationEvent()

data class SessionTimeout(override val action: Int = 0, override val extras: Bundle? = null) :
    NavigationEvent()

data class NoInternet(override val action: Int = 0, override val extras: Bundle? = null) :
    NavigationEvent()

data class OtherError(override val action: Int = 0, override val extras: Bundle? = null) :
    NavigationEvent()

data class ShareFile(override val action: Int = 0, override val extras: Bundle? = null) :
    NavigationEvent() {
    companion object {
        const val TITLE = "title"
        const val EXTRA = "extra"
        const val LINK = "link"
    }
}

data class ComingSoon(override val action: Int = 0, override val extras: Bundle? = null) :
    NavigationEvent()

data class InvalidLocalTime(override val action: Int = 0, override val extras: Bundle? = null) :
    NavigationEvent()

data class NavigateWithDeeplink(override val action: Int = 0, override val extras: Bundle? = null) :
    NavigationEvent()

data class BackToHome(override val action: Int = 0, override val extras: Bundle? = null) :
    NavigationEvent()

data class FinishJourney(override val action: Int = 0, override val extras: Bundle? = null) :
    NavigationEvent()

class ErrorEvent(val message: String? = null) : NavigationEvent()
