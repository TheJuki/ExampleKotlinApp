package com.thejuki.example.service

import android.util.Log

import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService
import com.thejuki.example.api.ApiClient

/**
 * Example Firebase Instance ID Service
 *
 * Provides the refreshed Firebase token to be updated with your server.
 *
 * @author **TheJuki** ([GitHub](https://github.com/TheJuki))
 * @version 1.0
 */
class ExampleFirebaseInstanceIDService : FirebaseInstanceIdService() {
    override fun onTokenRefresh() {
        // Get updated InstanceID token.
        val refreshedToken = FirebaseInstanceId.getInstance().token
        Log.d("FirebaseInstanceID", "Refreshed token: " + refreshedToken.orEmpty())

        // Update device token
        ApiClient.getInstance(this.applicationContext).updateDeviceRegistration(refreshedToken.orEmpty())
    }
}
