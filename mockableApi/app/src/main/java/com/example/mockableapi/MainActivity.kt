package com.example.mockableapi


import android.os.Bundle

import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    override fun onStart() {
        super.onStart()

        Apicalling.create().getUser()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe{  result ->

                val listView=findViewById<ListView>(R.id.ListView)
                listView.adapter=CustomListViewAdapter(this,result)


            }
    }

}