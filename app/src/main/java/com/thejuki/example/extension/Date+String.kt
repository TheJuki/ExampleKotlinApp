package com.thejuki.example.extension

import java.text.SimpleDateFormat
import java.util.*

/**
 * Date String Extensions
 *
 * Converts a Date to a String.
 *
 * @author **TheJuki** ([GitHub](https://github.com/TheJuki))
 * @version 1.0
 */
fun Date?.simpleDate(): String? {
    return if (this == null) null else SimpleDateFormat("MM/dd/yyyy", Locale.US).format(this)
}

fun Date?.simpleDateTime(): String? {
    return if (this == null) null else SimpleDateFormat("MM/dd/yyyy hh:mm a z", Locale.US).format(this)
}

fun Date?.simpleTime(): String? {
    return if (this == null) null else SimpleDateFormat("hh:mm:ss a", Locale.US).format(this)
}
