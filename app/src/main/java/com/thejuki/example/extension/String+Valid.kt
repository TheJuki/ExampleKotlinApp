package com.thejuki.example.extension

/**
 * String Valid Extensions
 *
 * Checks if a String length is greater than or equal to the length needed.
 *
 * @author **TheJuki** ([GitHub](https://github.com/TheJuki))
 * @version 1.0
 */
fun String?.valid(length: Int): Boolean {
    return this != null && this.trim().length >= length
}