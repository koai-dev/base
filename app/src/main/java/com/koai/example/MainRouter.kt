package com.koai.example

import android.content.Context
import com.koai.base.main.action.router.BaseRouter

interface MainRouter : BaseRouter {
    fun openSomeDestination(context: Context)
}