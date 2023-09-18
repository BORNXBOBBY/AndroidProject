package com.example.yati.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.yati.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_forget_password.*

class ForgetPasswordActivity : YatiActivity() {
    lateinit var email:EditText
    private  val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_password)

        email = findViewById(R.id.forgotPass_email)
        val sendEmailButton = findViewById<Button>(R.id.forgotPassword_Button)

        sendEmailButton.setOnClickListener {
            val emailText = email.text.toString()
            if (isEmailValid())
            else{
                forgotPass_email.error = "Invalid Email"
                forgotPass_email.requestFocus()
                return@setOnClickListener
            }

            forgotPassword()
        }
    }
    private fun forgotPassword(){
        val emailVal =email.text.toString().trim()

        this.displayLoaderActivity()
        firebaseAuth.sendPasswordResetEmail(emailVal)
            .addOnSuccessListener {
                Toast.makeText(this, "Check email to reset your password!", Toast.LENGTH_LONG).show()
                val intent = Intent(this,SigninActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Something wrong, please try after sometimes", Toast.LENGTH_SHORT).show()
            }
    }

    private fun isEmailValid():Boolean {
        val email = email.text.toString().trim()
        return email.matches(emailPattern.toRegex())
    }
}