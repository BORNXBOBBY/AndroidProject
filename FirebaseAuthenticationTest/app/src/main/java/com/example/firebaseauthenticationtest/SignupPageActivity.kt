package com.example.firebaseauthenticationtest

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputFilter
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText


class SignupPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup_page)

        val email = findViewById<EditText>(R.id.email)
        val password = findViewById<EditText>(R.id.edtText)

        val sign_Btn = findViewById<TextView>(R.id.btnCheck)

        sign_Btn.setOnClickListener {
            startActivity(Intent(this, HomePageActivity::class.java))
        }
//
//
//
//
        val login = findViewById<Button>(R.id.btnCheck)
        val sharedPreference = getSharedPreferences("userPref", Context.MODE_PRIVATE)
        val kumar = sharedPreference.edit()

        login.setOnClickListener {
            kumar.putBoolean("userLogin", true)
            kumar.apply()
            kumar.putString("email", email.text.toString()).commit()
            kumar.putString("password", password.text.toString()).commit()
        }


        val pass = findViewById<EditText>(R.id.edtText)
        val button = findViewById<Button>(R.id.btnCheck)
        val allowedCharacters =
            "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890!@#\$%^&*()_-+=[]{}|;:',.<>/?`~"


        pass.filters = arrayOf(CharacterInputFilter(allowedCharacters))
        pass.inputType = InputType.TYPE_TEXT_FLAG_MULTI_LINE or InputType.TYPE_CLASS_TEXT

        button.setOnClickListener {
            val text = pass.text.toString()
            val hasLowerCase = text.matches(Regex(".*[a-z].*"))
            val hasUpperCase = text.matches(Regex(".*[A-Z].*"))
            val hasDigit = text.matches(Regex(".*\\d.*"))
            val hasSpecialCharacter =
                text.matches(Regex(".*[!@#\$%^&*()\\-_+=\\[\\]{}|;:',.<>/?`~].*"))
            if (hasLowerCase && hasUpperCase && hasDigit && hasSpecialCharacter) {
                Toast.makeText(this, "Password is valid", Toast.LENGTH_SHORT).show()

                startActivity(Intent(this,HomePageActivity::class.java));
            } else {
                Toast.makeText(this, "Password is invalid", Toast.LENGTH_SHORT).show()
            }
        }

        val maxLength = 8
        val filterArray = arrayOf<InputFilter>(InputFilter.LengthFilter(maxLength))
        password.filters = filterArray


    }
}


