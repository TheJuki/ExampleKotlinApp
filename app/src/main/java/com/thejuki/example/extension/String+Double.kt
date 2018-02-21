package com.thejuki.example.extension

/**
 * String Double Extensions
 *
 * Provides a "safe" Double conversion.
 *
 * @author **TheJuki** ([GitHub](https://github.com/TheJuki))
 * @version 1.0
 */
fun String?.toSafeDouble(): Double {
    return if (this.isNullOrBlank()) {
        0.0
    } else {
        try {
            this!!.toDouble()
        } catch (e: NumberFormatException) {
            0.0
        }
    }
}