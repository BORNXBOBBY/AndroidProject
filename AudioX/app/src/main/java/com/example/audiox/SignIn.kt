package com.example.audiox

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast


class SignIn : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        val signIn = findViewById<TextView>(R.id.sig)
            val username = findViewById<EditText>(R.id.user)
            val passWord = findViewById<EditText>(R.id.password)
            val loginBtn = findViewById<Button>(R.id.register)

            val registerSharedPreferences = getSharedPreferences("userPref", Context.MODE_PRIVATE)
            val user = registerSharedPreferences.getString("name", "")
            val userPass = registerSharedPreferences.getString("password", "")

        signIn.setOnClickListener {
            startActivity(Intent(this, RegisterPage::class.java))
        }
            loginBtn.setOnClickListener {
                val userData: String = username.text.toString()
                val pass: String = passWord.text.toString()
                if (user == userData && userPass == pass) {
                    val edit = registerSharedPreferences.edit()
                    edit.putBoolean("userLogin", true)
                    edit.apply()
                    startActivity(Intent(this, RegisterHomePage::class.java))
                } else {
                    Toast.makeText(this, "Username/PasswordInvalid", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
