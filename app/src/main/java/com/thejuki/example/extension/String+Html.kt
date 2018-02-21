package com.thejuki.example.extension

import android.os.Build
import android.text.Html
import android.text.Spanned

/**
 * String HTML Extensions
 *
 * Provides a HTML Spanned markup object.
 *
 * @author **TheJuki** ([GitHub](https://github.com/TheJuki))
 * @version 1.0
 */
fun String?.fromHtml(): Spanned {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        return Html.fromHtml(this.orEmpty(), Html.FROM_HTML_MODE_COMPACT)
    } else {
        @Suppress("DEPRECATION")
        return Html.fromHtml(this.orEmpty())
    }
}