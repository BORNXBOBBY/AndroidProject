package com.example.audiox

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class jiosavvanSignupPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jiosavvan_signup_page)
        val login = findViewById<Button>(R.id.Signup)
        login.setOnClickListener {
            startActivity(Intent(this, RegisterPage::class.java))
        }
    }
}