package com.thejuki.example.extension

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * String Date Extensions
 *
 * Converts a String to a Date.
 *
 * @author **TheJuki** ([GitHub](https://github.com/TheJuki))
 * @version 1.0
 */
fun String?.toDate(): Date? {
    return if (this.isNullOrBlank()) null else {
        try {
            SimpleDateFormat("MM/dd/yyyy", Locale.US).parse(this.orEmpty())
        } catch (e: ParseException) {
            null
        }
    }
}

fun String?.toDateTime(): Date? {
    return if (this.isNullOrBlank()) null else {
        try {
            SimpleDateFormat("MM/dd/yyyy hh:mm a z", Locale.US).parse(this.orEmpty())
        } catch (e: ParseException) {
            null
        }
    }
}

fun String?.toTime(): Date? {
    return if (this.isNullOrBlank()) null else {
        try {
            SimpleDateFormat("hh:mm:ss a", Locale.US).parse(this.orEmpty())
        } catch (e: ParseException) {
            null
        }
    }
}
