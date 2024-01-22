package com.koai.base.utils

import android.content.Context
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat

object TextUtilsUtil {

    fun createClickableSpannable(
        context: Context,
        text: String,
        clickableText: String,
        textColorResId: Int,
        onClickListener: () -> Unit
    ): SpannableStringBuilder {
        val spannableStringBuilder = SpannableStringBuilder(text)
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                onClickListener.invoke()
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
            }
        }
        val start = text.indexOf(clickableText)
        val end = start + clickableText.length
        spannableStringBuilder.setSpan(clickableSpan, start, end, 0)
        spannableStringBuilder.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(context, textColorResId)),
            start,
            end,
            0
        )
        return spannableStringBuilder
    }

    fun setClickableText(view: TextView, spannableStringBuilder: SpannableStringBuilder) {
        view.text = spannableStringBuilder
        view.movementMethod = LinkMovementMethod.getInstance()
    }
}
