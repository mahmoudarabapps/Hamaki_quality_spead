package com.arabapps.hamaki

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.arabapps.hamaki.ui.activity.SplashActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.net.URL


class MyFirebaseMessagingService : FirebaseMessagingService() {


    override fun onMessageReceived(remoteMessage: RemoteMessage) {


        if (BuildConfig.DEBUG)  Log.d(TAG, "From: ${remoteMessage.from}")


        if (remoteMessage.data.isNotEmpty()) {

            if (BuildConfig.DEBUG)  Log.d(TAG, "Message data payload: ${remoteMessage.data}")

            sendNotification(
                remoteMessage.data.get("title").toString()
                ,
                remoteMessage.data.get("body").toString(),
                remoteMessage.data.get("image").toString()

            )
        } else {

            remoteMessage.notification?.let {
                if (BuildConfig.DEBUG)  Log.d(TAG, "Message Notification Body: ${it.body}")
                sendNotification(it.title.toString(), it.body.toString(), null)
            }
        }
        // Check if message contains a notification payload.

    }

    override fun onNewToken(token: String) {
        if (BuildConfig.DEBUG)  Log.d(TAG, "Refreshed token: $token")
    }


    private fun sendNotification(title: String, messageBody: String, imageUrl: String?) {
        val intent = Intent(this, SplashActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0 /* Request code */, intent,
            PendingIntent.FLAG_ONE_SHOT
        )

        val channelId = getString(R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.logo)
            .setContentTitle(title)
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setPriority(NotificationManager.IMPORTANCE_MAX)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }
        try {
            val url = URL(imageUrl.toString())
            val image = BitmapFactory.decodeStream(url.openConnection().getInputStream())
//            notificationBuilder.setStyle(
//                NotificationCompat.BigPictureStyle()
//                    .bigPicture(image)
//
//            )
            notificationBuilder.setLargeIcon(image)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            notificationBuilder.setStyle(NotificationCompat.BigTextStyle())
            println(e)
        }
        notificationManager.notify(0, notificationBuilder.build())
    }

    companion object {

        private const val TAG = "MyFirebaseMsgService"
    }
}