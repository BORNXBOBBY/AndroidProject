package com.example.whatsapp

import android.os.Build.VERSION_CODES.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout

class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)


        val tableLayout=findViewById<TabLayout>(R.id.tabLayot)
        val viewPager=findViewById<ViewPager>(R.id.framlayout)

        val adapter=TabAdapter(supportFragmentManager)
        adapter.addFragment(ChatFragment(),"CHAT")
        adapter.addFragment(StatusFragment(),"STATUS")
        adapter.addFragment(CallFragment(),"CALL")

        viewPager.adapter=adapter
        tableLayout.setupWithViewPager(viewPager)
    }
}