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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.constants.CHANNEL_ID
import com.artilearn.happygolfgo.constants.NOTIFICATION_ID
import com.artilearn.happygolfgo.ui.alram.AlramActivity
import com.artilearn.happygolfgo.ui.splash.SplashEmptyActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FCMMessage : FirebaseMessagingService(), LifecycleObserver {

    private var fcmIntent: Intent? = null
    private var defaultIntent: Intent? = null

    override fun onCreate() {
        super.onCreate()

        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }
    
    
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

        if (fcmIntent != null) {
            createNotification(body, fcmIntent!!)
        } else {
            createNotification(body, defaultIntent!!)
        }

    }

    private fun createNotification(body: MutableMap<String, String>, intent: Intent) {
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
            val name = getString(R.string.default_notification_channel_id)
            val descriptionText = getString(R.string.default_notification_text)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance)
                .apply {
                    description = descriptionText
                }

            val notificationManager =
                this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    private fun appCreate() {
        fcmIntent = Intent(this, SplashEmptyActivity::class.java)
            .apply {

            }
        defaultIntent = Intent(this, AlramActivity::class.java)
            .apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private fun appResume() {
        fcmIntent = Intent(this, AlramActivity::class.java)
            .apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
    }

}