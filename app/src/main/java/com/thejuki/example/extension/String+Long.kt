package com.thejuki.example.extension

/**
 * String Long Extensions
 *
 * Provides a "safe" Long conversion with valid check.
 *
 * @author **TheJuki** ([GitHub](https://github.com/TheJuki))
 * @version 1.0
 */
fun String?.toSafeLong(): Long {
    return if (this.isNullOrBlank()) {
        0L
    } else {
        try {
            this!!.toLong()
        } catch (e: NumberFormatException) {
            0L
        }
    }
}

fun String?.validId(): Boolean {
    return this.toSafeLong() > 0
}