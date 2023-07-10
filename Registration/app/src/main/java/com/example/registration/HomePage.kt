package com.example.registration

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomePage : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)
        val navbar=findViewById<BottomNavigationView>(R.id.bottomNavigation)
        val logoutBtn=findViewById<Button>(R.id.logout)
        LoadFragment(HomeFragment())
        navbar.setOnItemSelectedListener {
            when(it.itemId){
                R.id.home->LoadFragment(HomeFragment())
                R.id.setting->LoadFragment(SettingFragment())
                R.id.profile->LoadFragment(ProfileFragment())
            }
            true
        }
        logoutBtn.setOnClickListener {
            val sharedPreferences =getSharedPreferences("userPref", Context.MODE_PRIVATE)
            val editUserPref = sharedPreferences.edit()
            editUserPref.putBoolean("userLogin",false)
            editUserPref.apply()
           startActivity(Intent(this,Login::class.java))
        }
    }
    private fun LoadFragment(framgment: Fragment){
        supportFragmentManager.beginTransaction()
            .replace(R.id.fream,framgment).commit()
    }
}
