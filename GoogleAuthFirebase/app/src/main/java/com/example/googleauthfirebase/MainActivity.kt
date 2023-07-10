package com.example.googleauthfirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class MainActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    private var requestcode = 1234


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FirebaseApp.initializeApp(this)

        auth = FirebaseAuth.getInstance()
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        val googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)

        val signInButton = findViewById<SignInButton>(R.id.sign_in_button)

        signInButton.setOnClickListener {
            auth.signOut()
            val googleIntent = googleSignInClient.signInIntent
            startActivityForResult(googleIntent, requestcode)
        }
    }

    override fun onActivityResult(ActivityRequestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(ActivityRequestCode, resultCode, data)

        if (ActivityRequestCode == requestcode) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            task.addOnSuccessListener { it ->
                val credencial = GoogleAuthProvider.getCredential(it.idToken, null)
                auth.signInWithCredential(credencial)
                    .addOnSuccessListener {
                        Toast.makeText(this, "" + it.user?.displayName+" Succesfull", Toast.LENGTH_SHORT)
                            .show()
                    }
                    .addOnFailureListener {-
                        Toast.makeText(this, "" + it.message, Toast.LENGTH_SHORT).show()
                    }
            }
                .addOnFailureListener {
                    Toast.makeText(this, "" + it.message, Toast.LENGTH_SHORT).show()
                }
        }

    }
}