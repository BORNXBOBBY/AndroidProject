package com.example.a18augustsecondnotificationfirebase

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FirebaseMessageService(): FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("token", token)


    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        if(message!=null ){
            val chanelId = "notoficationsss"
            val channelName = "notoficationappssss"
            val imp = NotificationManager.IMPORTANCE_HIGH
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                val channel = NotificationChannel(chanelId, channelName, imp)

                val builder=Notification.Builder(this,chanelId)
                builder.setContentTitle(message.notification?.title)
                builder.setContentText(message.notification?.body)
                builder.setAutoCancel(true)
                builder.setPriority(Notification.PRIORITY_DEFAULT)
                builder.setSmallIcon(R.drawable.ic_launcher_background)

                val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)
                notificationManager.notify(0,builder.build())




            }
            else{
                val notificationBuilder = NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setContentTitle(message.notification!!.title)
                    .setContentText(message.notification!!.body)

                val notificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.notify(0,notificationBuilder.build())
            }

        }

    }

}