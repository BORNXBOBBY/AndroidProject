package com.example.audiox

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class HomePage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)
        val create = findViewById<TextView>(R.id.sign)
        val login = findViewById<TextView>(R.id.CreateButton)
        login.setOnClickListener {
            startActivity(Intent(this, jiosavvanSignupPage::class.java))
        }
            create.setOnClickListener {
                startActivity(Intent(this, SignIn::class.java))
        }
    }
}