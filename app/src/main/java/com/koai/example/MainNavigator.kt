package com.koai.example

import android.content.Context
import android.widget.Toast
import com.koai.base.main.action.navigator.BaseNavigator

class MainNavigator : BaseNavigator(), MainRouter {
    override fun openSomeDestination(context: Context) {
       Toast.makeText(context, " njnádasd", Toast.LENGTH_SHORT).show()
    }

}