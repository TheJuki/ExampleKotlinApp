package com.thejuki.example.extension

/**
 * Long Extensions
 *
 * Adds "OrZero", "toString", and "valid" methods to Long.
 *
 * @author **TheJuki** ([GitHub](https://github.com/TheJuki))
 * @version 1.0
 */
fun Long?.valueOrZero(): Long {
    return this ?: 0
}

// Used to avoid getting back the string "null" from toString()
fun Long?.stringValue(): String {
    return (this ?: 0).toString()
}

fun Long?.valid(): Boolean {
    return this != null && this > 0
}