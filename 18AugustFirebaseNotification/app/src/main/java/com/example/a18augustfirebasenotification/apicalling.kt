package com.example.a18augustfirebasenotification

import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.POST


interface Apicalling {
    @POST("fcm/send")
    fun send(@HeaderMap header: Map<String,String>, @Body notificationData : NotificationData) : Observable<NotificationModel>


    companion object {
        fun Create(): Apicalling {
            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())

                //cloud messaging api
                .baseUrl("https://fcm.googleapis.com/")
                .build()
            return retrofit.create(Apicalling::class.java)

        }
    }
    }