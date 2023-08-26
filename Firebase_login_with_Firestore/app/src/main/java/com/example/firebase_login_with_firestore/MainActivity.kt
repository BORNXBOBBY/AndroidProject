package com.example.firebase_login_with_firestore

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.text.InputFilter
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {
    lateinit var downloadUrl: String
    private lateinit var uri: Uri
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize Firebase Storage for get data
        val db = FirebaseFirestore.getInstance()


        val name = findViewById<EditText>(R.id.enter_name)
        val email = findViewById<EditText>(R.id.enter_email)
        val userId = findViewById<EditText>(R.id.enter_userid)
        val pass = findViewById<EditText>(R.id.enter_pass)
        val contact = findViewById<EditText>(R.id.enter_contact)

        val signUp = findViewById<Button>(R.id.signup)
        val Login = findViewById<Button>(R.id.login_btn)

        val allowedCharacters =
            "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890!@#\$%^&*()_-+=[]{}|;:',.<>/?`~"
        pass.filters = arrayOf(CharacterInputFilter(allowedCharacters))

        pass.inputType = InputType.TYPE_TEXT_FLAG_MULTI_LINE or InputType.TYPE_CLASS_TEXT
        val maxLength = 8
        val filterArray = arrayOf<InputFilter>(InputFilter.LengthFilter(maxLength))
        pass.filters = filterArray

        signUp.setOnClickListener {

            val text = pass.text.toString()
            val hasLowerCase = text.matches(Regex(".*[a-z].*"))
            val hasUpperCase = text.matches(Regex(".*[A-Z].*"))
            val hasDigit = text.matches(Regex(".*\\d.*"))
            val hasSpecialCharacter =
                text.matches(Regex(".*[!@#\$%^&*()\\-_+=\\[\\]{}|;:',.<>/?`~].*"))


            if (hasLowerCase && hasUpperCase && hasDigit && hasSpecialCharacter) {
                val data = hashMapOf(
                    "name" to name.text.toString(),
                    "email" to email.text.toString(),
                    "userId" to userId.text.toString(),
                    "pass" to pass.text.toString(),
                    "contact" to contact.text.toString(),
                )


                db.collection("CollegeStudent").add(data)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()
                        name.text.clear()
                        email.text.clear()
                        userId.text.clear()
                        pass.text.clear()
                        contact.text.clear()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this, "Invalid password format", Toast.LENGTH_SHORT).show()
            }

            Login.setOnClickListener {
                startActivity(Intent(this, LoginActivity::class.java))
            }


        }
    }
}