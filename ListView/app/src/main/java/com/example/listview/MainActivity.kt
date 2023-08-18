package com.example.listview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val lang= arrayOf<String>("State","c++","kotlin","java")

        val list= findViewById<ListView>(R.id.spinner_second)

        val adapt = ArrayAdapter(this, R.layout.spinnerpage, lang)
        list.adapter = adapt

    }
}