package com.thejuki.example.extension

import android.content.Context
import android.support.annotation.ColorInt

/**
 * Int Color Extensions
 *
 * Handles getting back a @ColorInt from a @StyleableRes.
 *
 * @author **TheJuki** ([GitHub](https://github.com/TheJuki))
 * @version 1.0
 */
@ColorInt
fun Int.getThemeAttrColor(context: Context): Int {
    val array = context.obtainStyledAttributes(null, intArrayOf(this))
    try {
        return array.getColor(0, 0)
    } finally {
        array.recycle()
    }
}