package com.koai.base.main.extension

import android.app.Activity
import android.widget.EditText
import androidx.fragment.app.Fragment

fun Fragment.showKeyboard(
    activity: Activity,
    view: EditText,
) {
    activity.showKeyboard(view)
}

fun Fragment.closeKeyBoard(activity: Activity) {
    activity.closeKeyBoard()
}
