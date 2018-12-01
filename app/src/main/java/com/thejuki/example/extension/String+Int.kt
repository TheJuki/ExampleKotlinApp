package com.thejuki.example.extension

/**
 * String Int Extensions
 *
 * Provides a "safe" Int conversion.
 *
 * @author **TheJuki** ([GitHub](https://github.com/TheJuki))
 * @version 1.0
 */
fun String?.toSafeInt(): Int {
    return if (this.isNullOrBlank()) {
        0
    } else {
        try {
            this.toInt()
        } catch (e: NumberFormatException) {
            0
        }
    }
}