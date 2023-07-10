package com.example.tabularform_whatsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TableLayout
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        val tableLayout=findViewById<TabLayout>(R.id.tabLayot)
//        val viewPager=findViewById<ViewPager>(R.id.framlayout)
//
//        val adapter=TabAdapter(supportFragmentManager)
//        adapter.addFragment(ChatFragment(),"CHAT")
//        adapter.addFragment(StatusFragment(),"STATUS")
//        adapter.addFragment(CallFragment(),"CALL")
//
//        viewPager.adapter=adapter
//        tableLayout.setupWithViewPager(viewPager)


//        bottom navigation code

        LoadFragment(ChatFragment())
        val buttonNav =findViewById<BottomNavigationView>(R.id.bottomNav)
        buttonNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> LoadFragment(ChatFragment())
                R.id.profile -> LoadFragment(CallFragment())
                R.id.setting -> LoadFragment(StatusFragment())
            }
            true
        }
    }
    private fun LoadFragment(fragment:Fragment){
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment).commit()
    }
}