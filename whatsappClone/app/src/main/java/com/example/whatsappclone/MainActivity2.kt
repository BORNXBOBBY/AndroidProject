package com.example.whatsappclone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import android.widget.TableLayout
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout


class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        val tableLayout=findViewById<TabLayout>(R.id.tablayout)
        val viewPager=findViewById<ViewPager>(R.id.framlayout)

        val adapter=TabAdapter(supportFragmentManager)
        adapter.addFragment(ChatFragment(),"CHAT")
        adapter.addFragment(StatusFragment(),"STATUS")
        adapter.addFragment(CallFragment(),"CALL")

        viewPager.adapter=adapter
        tableLayout.setupWithViewPager(viewPager)
    }
}