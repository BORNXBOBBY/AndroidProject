package com.example.firebase_login_with_firestore

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.ContentValues
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val db= FirebaseFirestore.getInstance()
        val loginEmail=findViewById<EditText>(R.id.enter_email)
        val loginPassword=findViewById<EditText>(R.id.enter_password)
        val loginBtn=findViewById<Button>(R.id.login)


        loginBtn.setOnClickListener {
            val userEmail: String = loginEmail.text.toString()
            val userPass: String = loginPassword.text.toString()

            db.collection("CollegeStudent").get()
                .addOnSuccessListener { snapshot ->
                    var isUserAuthenticated = false

                    for (document in snapshot.documents) {
                        val email = document.getString("email")
                        val password = document.getString("pass")

                        if (email == userEmail && password == userPass) {
                            isUserAuthenticated = true
                            break
                        }
                    }

                    if (isUserAuthenticated) {
                        Toast.makeText(this, "Successfully logged in/welcome", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this, "Username/Password Invalid", Toast.LENGTH_LONG).show()
                    }
                }
                .addOnFailureListener { e ->
                    Log.e(ContentValues.TAG, "Error retrieving data from Firestore", e)
                }
        }
    }
}