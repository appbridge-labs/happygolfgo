package com.artilearn.happygolfgo.api.firebase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.ui.home.HomeActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FCMMessage : FirebaseMessagingService() {

    private val CHANNEL_ID = "HappyGolf channel"
    private val NOTIFICATION_ID = 9000

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        remoteMessage.data.let {
            sendNotification(remoteMessage.data)
        }
    }

    override fun onNewToken(to: String) {
        super.onNewToken(to)
    }

    private fun sendNotification(body: MutableMap<String, String>) {
        createNotificationChannel()

        val intent = Intent(this, HomeActivity::class.java)
        intent.putExtra("fcm", 9000)
        intent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP

        val sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val pendingIntent = PendingIntent.getActivity(
            this,
            NOTIFICATION_ID,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_appnotifi)
            .setContentTitle("${body["title"]}")
            .setContentText("${body["body"]}")
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_VIBRATE)
            .setSound(sound)
            .setAutoCancel(true)

        NotificationManagerCompat.from(this).notify(NOTIFICATION_ID, notification.build())
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "????"
            val descriptionText = "Channel description"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }

            val notificationManager =
                this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

}