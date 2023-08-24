package com.example.classtest24august

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val  arrayList= arrayListOf<DataModel>(DataModel("krishan"),
            DataModel("Rahul"),
            DataModel("Abhimanyu"))

        val listView=findViewById<ListView>(R.id.list)
//        val adapter=Adapter(this,arrayList)
//        listView.adapter=adapter

        val adapter=Adapter(this,arrayList)

        listView.adapter=adapter

    }
}