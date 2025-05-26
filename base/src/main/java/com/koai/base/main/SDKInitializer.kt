package com.koai.base.main

import com.koai.base.main.action.navigator.BaseNavigator

/**
 * a shared contract for initialization
 * How to use:
 * - create journeySDK object implement this
 * - call init() in KoinApp di
 * Eg:
 *object HomeSDKInit : SDKInitializer {
 *     lateinit var navigator: NavigatorViewModel
 *     override fun init(navigatorViewModel: NavigatorViewModel) {
 *         navigator = navigatorViewModel
 *     }
 * }
 *
 */
interface SDKInitializer {
    fun init(navigator: BaseNavigator)
}
