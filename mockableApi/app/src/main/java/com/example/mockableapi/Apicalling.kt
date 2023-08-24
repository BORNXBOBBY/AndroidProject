package com.example.mockableapi

import android.database.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface Apicalling {
    @GET("customer")


    fun getUser(): Observable<List<DataModel>>
    companion object factory{
        fun  create():Apicalling{
            val retrofit= Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://demo1796856.mockable.io/")
                .build()
            return retrofit.create(Apicalling::class.java)

        }

    }

}