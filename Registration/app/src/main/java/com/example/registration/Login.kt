package com.example.registration

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val signUp = findViewById<TextView>(R.id.sign_up)
        val username = findViewById<AppCompatEditText>(R.id.username)
        val passWord = findViewById<AppCompatEditText>(R.id.password)
        val loginBtn = findViewById<Button>(R.id.btn)

        val registerSharedPreferences = getSharedPreferences("userPref", Context.MODE_PRIVATE)
        val user = registerSharedPreferences.getString("name", "")
        val userPass = registerSharedPreferences.getString("password", "")

        signUp.setOnClickListener {
            startActivity(Intent(this, Register::class.java))
        }
        loginBtn.setOnClickListener {
            val userData: String = username.text.toString()
            val pass: String = passWord.text.toString()
            if (user == userData && userPass == pass) {
                val edit = registerSharedPreferences.edit()
                edit.putBoolean("userLogin", true)
                edit.apply()
                startActivity(Intent(this, HomePage::class.java))
            }
            else{
                Toast.makeText(this, "Username/PasswordInvalid", Toast.LENGTH_SHORT).show()
            }

        }
    }
}