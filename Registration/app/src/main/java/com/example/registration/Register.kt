package com.example.registration

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.AppCompatEditText

class Register : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        val name=findViewById<AppCompatEditText>(R.id.name)
        val mobile=findViewById<AppCompatEditText>(R.id.mobile)
        val email=findViewById<AppCompatEditText>(R.id.email)
        val password=findViewById<AppCompatEditText>(R.id.password)

        val sign_Btn=findViewById<TextView>(R.id.sign_in)

        sign_Btn.setOnClickListener {
            startActivity(Intent(this,Login::class.java))
        }
        val login=findViewById<Button>(R.id.signup)
        val sharedPreference=getSharedPreferences("userPref",Context.MODE_PRIVATE)
        val kumar=sharedPreference.edit()
        login.setOnClickListener {
            kumar.putBoolean("userLogin", true)
            kumar.apply()
            kumar.putString("name",name.text.toString()).commit()
            kumar.putString("mobile",mobile.text.toString()).commit()
            kumar.putString("email",email.text.toString()).commit()
            kumar.putString("password",password.text.toString()).commit()
            startActivity(Intent(this,HomePage::class.java))
        }
    }
}