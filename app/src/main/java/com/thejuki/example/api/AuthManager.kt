package com.thejuki.example.api

import android.content.Context
import android.content.SharedPreferences
import com.thejuki.example.PreferenceConstants
import com.thejuki.example.extension.PreferenceHelper

/**
 * Auth Manager
 *
 * Handles user role checking.
 *
 * @author **TheJuki** ([GitHub](https://github.com/TheJuki))
 * @version 1.0
 */
class AuthManager private constructor(context: Context) {
    var prefs: SharedPreferences? = null
    private var roles = mutableSetOf<String>()

    init {
        prefs = PreferenceHelper.securePrefs(context)
        refresh()
    }

    fun refresh() {
        roles = prefs!!.getStringSet(PreferenceConstants.rolesData, mutableSetOf<String>()) ?: mutableSetOf()
    }

    fun has(role: String): Boolean {
        return roles.contains(role)
    }

    companion object : SingletonHolder<AuthManager, Context>(::AuthManager)
}
