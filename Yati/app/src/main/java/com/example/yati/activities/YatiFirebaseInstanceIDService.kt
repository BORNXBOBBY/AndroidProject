package com.example.yati.activities

import android.content.Intent
import android.util.Log
import com.example.yati.fragments.ProductsFragment
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class YatiFirebaseInstanceIDService:FirebaseMessagingService(){
    val TAG = "FCM Service"
    override fun onMessageReceived(remoteMessage:  RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        Log.d(TAG,"From "+ remoteMessage.from)
        Log.d(TAG,"Notification Massage Body  "+ remoteMessage.from)

        val intent = Intent(this@YatiFirebaseInstanceIDService,MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra("fcm_massage", remoteMessage.notification?.body)
        startActivity(intent)

    }

}