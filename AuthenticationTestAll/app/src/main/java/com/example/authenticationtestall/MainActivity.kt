package com.example.authenticationtestall

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Handler().postDelayed({
            startActivity(Intent(this,FirebaseAuthActivity::class.java))
            val splashSharedPreferences =getSharedPreferences("userPref", Context.MODE_PRIVATE)
            val loginStatus = splashSharedPreferences.getBoolean("userLogin",false)
            if (loginStatus)
            {
                val intent = Intent(this, FirebaseAuthActivity::class.java)
                startActivity(intent)
                finish()
            }

        },2000)

    }
    }
