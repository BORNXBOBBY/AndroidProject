package com.example.testall

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.hbb20.CountryCodePicker
import com.google.firebase.auth.ktx.auth
import java.util.concurrent.TimeUnit


class LoginPageActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth

    lateinit var phoneNumber: String
    private lateinit var vId: String
    private var requestcode = 1234

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)
        auth = Firebase.auth
        FirebaseApp.initializeApp(this)
        auth = FirebaseAuth.getInstance()

        val email=findViewById<EditText>(R.id.email)
        val phone=findViewById<EditText>(R.id.phone)
        val signUp=findViewById<Button>(R.id.signup)
        val signIn=findViewById<Button>(R.id.signin)


        val phoneno = findViewById<EditText>(R.id.phone_number)
        val ccp = findViewById<CountryCodePicker>(R.id.ccp)
        val sendOtpButton = findViewById<Button>(R.id.sendOtp)



        signUp.setOnClickListener {
            auth.createUserWithEmailAndPassword(email.text.toString() , phone.text.toString())
                .addOnSuccessListener {
                    Toast.makeText(this, "create account auth", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener{
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
        }
        signIn.setOnClickListener{
            auth.signInWithEmailAndPassword(email.text.toString() , phone.text.toString())
                .addOnSuccessListener {

                    val auth=FirebaseAuth.getInstance().currentUser?.uid

                    val db = FirebaseFirestore.getInstance()
                    val user = hashMapOf(
                        "name" to it.user?.displayName,
                        "image" to it.user?.photoUrl,
                        "email" to it.user?.email,
                        "number" to it.user?.phoneNumber
                    )
                    // Add a new document with a generated ID
                    if ( auth != null) {
                        db.collection("users").document( auth)
                            .set(user)
                            .addOnSuccessListener {
                                Log.d(TAG, "DocumentSnapshot added with ID")
                                startActivity(Intent(this,SignUpActivity::class.java))

                            }
                            .addOnFailureListener { e ->
                                Log.w(TAG, "Error adding document", e)
                            }
                    }
                    Toast.makeText(this, "Successfully Login", Toast.LENGTH_SHORT).show()

                }.addOnFailureListener{
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
        }

        //        for phone auth
        sendOtpButton.setOnClickListener {
            ccp.registerCarrierNumberEditText(phoneno)
            phoneNumber = ccp.fullNumberWithPlus.replace(" ", "")
            initiateOtp()
        }
        // verify otp


        val enterOtp=findViewById<EditText>(R.id.otp)
        val verifyButton=findViewById<Button>(R.id.verifyOtp)
        verifyButton.setOnClickListener {

            val credential = PhoneAuthProvider.getCredential(vId, enterOtp.text.toString())
            auth.signInWithCredential(credential)
                .addOnSuccessListener {

                    val auth=FirebaseAuth.getInstance().currentUser?.uid

                    val db = FirebaseFirestore.getInstance()
                    val user = hashMapOf(
                        "name" to it.user?.displayName,
                        "image" to it.user?.photoUrl,
                        "email" to it.user?.email,
                        "number" to it.user?.phoneNumber
                    )
                    // Add a new document with a generated ID
                    if ( auth != null) {
                        db.collection("users").document( auth)
                            .set(user)
                            .addOnSuccessListener {
                                Log.d(TAG, "DocumentSnapshot added with ID")
                                startActivity(Intent(this,SignUpActivity::class.java))

                            }
                            .addOnFailureListener { e ->
                                Log.w(TAG, "Error adding document", e)
                            }
                    }

                    Toast.makeText(this, "verified", Toast.LENGTH_SHORT).show()
                    Toast.makeText(this, "Registation successfull", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this,SignUpActivity::class.java))
                }
                .addOnFailureListener {
                    Toast.makeText(this, "not verify", Toast.LENGTH_SHORT).show()
                }

            enterOtp.text.clear()
        }

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

    // send otp message
    private fun initiateOtp() {

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(30L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                override fun onCodeSent(
                    verificationId: String,
                    forceResendingToken: PhoneAuthProvider.ForceResendingToken
                ) {
                    vId = verificationId
                    Toast.makeText(this@LoginPageActivity, "send $verificationId", Toast.LENGTH_SHORT).show()

                }

                override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {

                }

                override fun onVerificationFailed(e: FirebaseException) {

                }

            })

            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)

    }
    override fun onActivityResult(ActivityRequestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(ActivityRequestCode, resultCode, data)

        if (ActivityRequestCode == requestcode) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            task.addOnSuccessListener { it ->
                val credencial = GoogleAuthProvider.getCredential(it.idToken, null)
                auth.signInWithCredential(credencial)
                    .addOnSuccessListener {

                        val auth=FirebaseAuth.getInstance().currentUser?.uid

                        val db = FirebaseFirestore.getInstance()
                        val user = hashMapOf(
                            "name" to it.user?.displayName,
                            "image" to it.user?.photoUrl,
                            "email" to it.user?.email,
                            "number" to it.user?.phoneNumber
                        )
                        // Add a new document with a generated ID
                        if ( auth != null) {
                            db.collection("users").document( auth)
                                .set(user)
                                .addOnSuccessListener {
                                    Log.d(TAG, "DocumentSnapshot added with ID")
                                    startActivity(Intent(this,SignUpActivity::class.java))

                                }
                                .addOnFailureListener { e ->
                                    Log.w(TAG, "Error adding document", e)
                                }
                        }
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "" + it.message, Toast.LENGTH_SHORT).show()
                    }
            }
                .addOnFailureListener {
                    Toast.makeText(this, "" + it.message, Toast.LENGTH_SHORT).show()
                }
        }


    }
}