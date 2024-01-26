package com.koai.example.app

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.view.Gravity
import android.widget.GridLayout
import android.widget.TextView
import com.koai.example.R

object TextViewUtils {

    fun createTextView(
        context: Context,
        text: String,
        textSize: Float,
        typeface: Typeface,
        backgroundDrawable: Drawable,
        textColor: Int
    ): TextView {
        val textView = TextView(context)
        val layoutParams = GridLayout.LayoutParams()
        layoutParams.rightMargin = 14
        layoutParams.height = context.resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._32sdp)
        layoutParams.width = context.resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._32sdp)
        layoutParams.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
        layoutParams.setGravity(Gravity.CENTER)
        textView.layoutParams = layoutParams
        textView.text = text
        textView.textSize = textSize
        textView.typeface = typeface
        textView.background = backgroundDrawable
        textView.setTextColor(textColor)
        textView.gravity = Gravity.CENTER
        textView.isClickable = true
        textView.isFocusable = true

        return textView
    }
}