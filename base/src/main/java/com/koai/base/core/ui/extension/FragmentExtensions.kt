package com.koai.base.core.ui.extension

import android.app.Activity
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.koai.base.app.BaseActivity

fun Fragment.showKeyboard(
    activity: Activity,
    view: EditText,
) {
    activity.showKeyboard(view)
}

fun Fragment.closeKeyBoard(activity: Activity) {
    activity.closeKeyBoard()
}

fun Fragment.withSafeContext(block: (context: BaseActivity<*, *, *>) -> Unit) {
    activity?.let {
        try {
            block.invoke(it as BaseActivity<*, *, *>)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
