package com.example.a18augustfirebasenotification

import android.annotation.SuppressLint
import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var title=findViewById<EditText>(R.id.title)
        var descreiption=findViewById<EditText>(R.id.description)
        var  button=findViewById<Button>(R.id.sendNotification)



        FirebaseMessaging.getInstance().token.addOnSuccessListener {
            Log.d("Bobby", it.toString())

            val user = hashMapOf(
                "token" to it.toString()
            )

// Add a new document with a generated ID
            FirebaseFirestore.getInstance().collection("user")
                .add(user)
                .addOnSuccessListener { documentReference ->
                    Log.d(ContentValues.TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                }
                .addOnFailureListener { e ->
                    Log.w(ContentValues.TAG, "Error adding document", e)
                }
        }


        val tokenList: List<String> =
            mutableListOf(
                "fLpaEhu-SIy5TVl33N2wUJ:APA91bGuRbt3JKCA2BoHP4jlZ9jn46IBKzQbpGoOIwWKxl4uKtqUC_j2VK1kMqpLa_4M1OT-RPxameoAYnzuWo8QbcRol1RoABByT0hvWt2zh1wkc-0PXXrwa3TbKEvc2n7kpJFCAica"
                ,"fS5VVBzpSimpPNILx9K2iA:APA91bH8tINDUNjG7QCyRMyMhiQnytmiOxjKYD6UCfIKVpAyIgXZ6Ve1K-MEpwj-an2tPHBcAYtdUM8qhohn-wTBeZOsCbEx5r5PoxMIsM2eSmszOgI16zXo4IdI0cMsVTqNNeJy0xwG",
                "cmM5Ewo-TYmAFdEaXQKhl6:APA91bGN1yrCvg5mnwSOf4GkYRKqli9jhVFF8nKMJYcpzNW8e4MHdyQIVFirkeoeE55Lt6lX-vS1tCgBD5irsrsC9QPRnmjhw51G7ttcrkvNttxFedacqHPjv5HXYlYuWBsYOvJ-T3FH",
                "cjX-IgP6QEC9-TYK-NUA1U:APA91bF5DYu_yHGtP9EItWhb6-hXIMnVH4tMBQ87SU0jzdaAUVKjTe1cNhpdUcvGuAbiz5mu_BDikghKnDsD5UUB4wwfSQc0Z1vWwQG-p75q7HkmEGbIHzUyyJsXkazJBTE9TK8D7of9",
                "epbT0tbTR1qTG1HkubzLW-:APA91bHRLUr81svAPYNGAPyzeozTeCYg6Jh4BJuCDAg8V_Kgu_Uigt2LM-52NKuplr0z9Ki5KgVmwQ37ehav_w0qXMBF3EBIEr4i6oN-UazjLuWeihZGvg3yZvHT8CirJmwbMYeblztO",
                " crVPyDCFTuKYFTrk5r5mN2:APA91bGsMsL15xrAsebK4oDGyXwBQjwY7yshxqO8zpJaWNZDm4E4lqtZ2fOsBtyxOaFUbJmUD76Gl2gaT5kcmvclIDjvYdxpD3T7ROeHSzY7an-m7LDzlSYCXQV7aPfOjH4rZzcDqaAc")
        val headerMap =
            hashMapOf<String, String>("Authorization" to "key=AAAAjUQGCaE:APA91bFg3cq8PkxYrWl4iQZF071HQmFqLBiB2rKwoHOFrSmArSPyU3H3EyNlopx_fBx-959eCywFSGav5Hh24fxBHxjAwLkReBK60F5-R49P_xEgdTl9bL7o3iz0PNr7lg0PgIy6FjQJ")



        button.setOnClickListener{

            val notificationData = NotificationData(
                Notification(
                    "notoficationsss",
                    title.text.toString(),
                    true,
                    descreiption.text.toString()
                ), tokenList
            )
            Apicalling.Create().send(headerMap, notificationData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    val notification = it
                }

        }


    }
}