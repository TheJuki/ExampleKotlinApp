package com.thejuki.example.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import com.crashlytics.android.Crashlytics
import com.github.omadahealth.lollipin.lib.managers.LockManager
import com.thejuki.example.PreferenceConstants
import com.thejuki.example.R
import com.thejuki.example.api.ApiClient
import com.thejuki.example.api.AuthManager
import com.thejuki.example.api.CustomRxJavaErrorHandler
import com.thejuki.example.extension.PreferenceHelper

/**
 * Main Activity
 *
 * The activity that loads after the Splash. Handles main initialization and Login check.
 *
 * @author **TheJuki** ([GitHub](https://github.com/TheJuki))
 * @version 1.0
 */
class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        CustomRxJavaErrorHandler.setErrorHandler()

        // Initialize API client
        ApiClient.getInstance(this)


        // LolliPin
        val defPrefs = PreferenceHelper.defaultPrefs(this)
        if (defPrefs.getBoolean(PreferenceConstants.usePasscode, false)) {
            val lockManager = LockManager.getInstance()
            lockManager.enableAppLock(this, CustomPinLockActivity::class.java)
            lockManager.appLock.timeout = 1000
            lockManager.appLock.logoId = R.drawable.ic_memory_black_64dp
            lockManager.appLock.isFingerprintAuthEnabled = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
        }

        // Create channel to show notifications.
        @SuppressLint("NewApi")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = getString(R.string.default_notification_channel_id)
            val channelName = getString(R.string.default_notification_channel_name)
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(NotificationChannel(channelId,
                    channelName, NotificationManager.IMPORTANCE_LOW))
        }

        val prefs = PreferenceHelper.securePrefs(this)

        if (prefs.getBoolean(PreferenceConstants.loggedIn, false)) {
            // Initialize roles manager
            AuthManager.getInstance(this)

            Crashlytics.setUserIdentifier(ApiClient.getInstance(this).getUsername())
            val intent = Intent(this, DrawerActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
