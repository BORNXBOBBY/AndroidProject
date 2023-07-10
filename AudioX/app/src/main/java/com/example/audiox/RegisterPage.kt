package com.example.audiox

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView


class RegisterPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_page)


            val user = findViewById<EditText>(R.id.user)
            val password = findViewById<EditText>(R.id.password)
            val email = findViewById<EditText>(R.id.email)
            val mobile = findViewById<EditText>(R.id.mobile)

            val sign = findViewById<TextView>(R.id.sig)

        sign.setOnClickListener {
            startActivity(Intent(this, SignIn::class.java))
        }

            val login = findViewById<Button>(R.id.Registerr)
            val sharedPreference = getSharedPreferences("userPref", Context.MODE_PRIVATE)
            val kumar = sharedPreference.edit()
            login.setOnClickListener {
                kumar.putBoolean("userLogin", true)
                kumar.apply()
                kumar.putString("user", user.text.toString()).commit()
                kumar.putString("password", password.text.toString()).commit()
                kumar.putString("email", email.text.toString()).commit()
                kumar.putString("mobile", mobile.text.toString()).commit()
                startActivity(Intent(this, RegisterHomePage::class.java))
            }
        }
    }
