package com.example.bottomnavigationbutton

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
    private fun LoadFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment).commit()
    }
    }
