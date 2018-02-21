package com.thejuki.example.extension

import android.os.Bundle
import android.os.Parcel

/**
 * Custom Pin Lock Activity
 *
 * Handles the success, failure, and forgot dialog of AppLockActivity
 *
 * @author **TheJuki** ([GitHub](https://github.com/TheJuki))
 * @version 1.0
 */
fun Parcel.readBundleMap(): Map<String, String> {
    val bundle = this.readBundle(String::class.java.classLoader)
    val map = HashMap<String, String>()
    for (key in bundle.keySet()) {
        map[key] = bundle.getString(key)
    }
    return map
}

fun Parcel.writeBundleMap(map: Map<String, String>) {
    val bundle = Bundle()
    for (key in map.keys) {
        bundle.putString(key, map[key])
    }
    this.writeBundle(bundle)
}
