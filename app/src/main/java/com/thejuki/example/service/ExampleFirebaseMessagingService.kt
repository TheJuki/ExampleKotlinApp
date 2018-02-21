package com.thejuki.example.service

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.support.v4.app.NotificationCompat
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.thejuki.example.R
import com.thejuki.example.activity.MainActivity
import com.thejuki.example.extension.toSafeLong

/**
 * Example Firebase Messaging Service
 *
 * Provides the Firebase notification handler.
 *
 * @author **TheJuki** ([GitHub](https://github.com/TheJuki))
 * @version 1.0
 */
class ExampleFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ

        if (remoteMessage == null) {
            Log.e("FirebaseMessaging", "Remote message is null")
            return
        }

        Log.d("FirebaseMessaging", "From: " + remoteMessage.from)

        // Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {
            Log.d("TCFirebaseMessaging", "Message data payload: " + remoteMessage.data)
            val messageBody = remoteMessage.data["message"]
            val messageCount = remoteMessage.data["message_count"].toSafeLong()
            sendNotification(messageBody.orEmpty(), messageCount.toInt())
        }

        // Check if message contains a notification payload.
        // In my case, this was not needed
        if (remoteMessage.notification != null) {
            Log.d("FirebaseMessaging", "Message Notification Body: " + remoteMessage.notification?.body!!)
        }
    }

    private fun sendNotification(messageBody: String, messageCount: Int) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT)

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val channelId = getString(R.string.default_notification_channel_id)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_stat_name)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setNumber(messageCount)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }
}
