package com.task.productcatalog.util

import android.os.Build
import android.text.*
import android.text.style.URLSpan
import android.widget.TextView

fun TextView.formatHtmlWithoutLinks(string: String) {
    text = parseHtml(string)
    removeUnderlines()
}

fun parseHtml(string: String): Spanned {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(string, Html.FROM_HTML_MODE_COMPACT)
    } else {
        Html.fromHtml(string)
    }
}

fun TextView.removeUnderlines() {
    val s: Spannable = SpannableString(text)
    val spans = s.getSpans(0, s.length, URLSpan::class.java)
    for (span in spans) {
        val start = s.getSpanStart(span)
        val end = s.getSpanEnd(span)
        s.removeSpan(span)
        val newSpan = URLSpanNoUnderline(span.url)
        s.setSpan(newSpan, start, end, 0)
    }
    text = s
}

class URLSpanNoUnderline(url: String?) : URLSpan(url) {
    override fun updateDrawState(ds: TextPaint) {
        super.updateDrawState(ds)
        ds.isUnderlineText = false
    }
}
