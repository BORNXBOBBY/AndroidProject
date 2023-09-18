package com.example.yati.activities

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.example.yati.R
import com.example.yati.global.AppGlobal
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_signin.*
import java.util.concurrent.TimeUnit


class SigninActivity : YatiActivity(){
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var phoneAuth : PhoneAuthProvider
    lateinit var sendOtpEditText : EditText
    lateinit var sendOtpLayout : RelativeLayout
    lateinit var signinLayout: LinearLayout
    lateinit var toolbar : Toolbar
    lateinit var phoneNumber : EditText
    lateinit var password: EditText
    lateinit var verifyButton: Button
    private var Tag ="splace"
    private var verificationedId: String? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)
        firestore = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()
        phoneAuth = PhoneAuthProvider.getInstance()

        Firebase.dynamicLinks
            .getDynamicLink(intent)
            .addOnSuccessListener(this) { pendingDynamicLinkData ->
                // Get deep link from result (may be null if no link is found)
                var deepLink: Uri? = null
                if (pendingDynamicLinkData != null) {
                    deepLink = pendingDynamicLinkData.link
                    val linkArray =deepLink.toString().split("/").toTypedArray()
                    val linkSize=linkArray.size // 7
                    val referralUserId = linkArray.get(linkSize-1).toInt()
                    AppGlobal.referralUserId = referralUserId.toString()
                    Log.e(Tag," My refer link " + deepLink.toString())
                    Log.e(Tag," Referral user id " + linkArray.get(6).toInt())
                }
            }
            .addOnFailureListener(this) { e -> Log.w(Tag, "getDynamicLink:onFailure", e) }

        val registerText = findViewById<TextView>(R.id.register_textView)
        registerText.setOnClickListener {
            val intent = Intent(this,RegisterActivity::class.java)
            startActivity(intent)
        }
        val forgetPassword = findViewById<TextView>(R.id.forgetPassword_textView)
        forgetPassword.setOnClickListener {
            val intent = Intent(this, ForgetPasswordActivity::class.java)
            startActivity(intent)
        }

        val siginButton = findViewById<Button>(R.id.login_button)
        phoneNumber = findViewById(R.id.name_editText)
        sendOtpEditText = findViewById(R.id.signinOtp_editText)
        sendOtpLayout = findViewById(R.id.signinOtp_layout)
        signinLayout = findViewById(R.id.signin_layout)
        toolbar = findViewById(R.id.signin_toolbar)
        verifyButton = findViewById(R.id.signinOtp_button)

        verifyButton.setOnClickListener {
            val otpEditText = sendOtpEditText.text.toString().trim()

            if (otpEditText.isEmpty() || otpEditText.length < 6){
                signinOtp_editText.error = "Code required"
                signinOtp_editText.requestFocus()
                return@setOnClickListener
            }
            verificationedId?.let {
                val credential = PhoneAuthProvider.getCredential(it, otpEditText)
                addPhoneNumber(credential)
            }
        }

        siginButton.setOnClickListener {
            val number = phoneNumber.text.toString()

            if (number.isEmpty()) {
                phoneNumber.error = "Please Enter Phone Number!"
                phoneNumber.requestFocus()
                return@setOnClickListener
            }
            if (isOnline()) {
                displayLoaderActivity()
                sendPhoneNumberVerificationCode()
            }else{
                showAlertDialog()
            }
        }
    }

    private fun sendPhoneNumberVerificationCode(){
        val number = phoneNumber.text.toString().trim()
        phoneAuth.verifyPhoneNumber("+91$number",60, TimeUnit.SECONDS, this , phoneAuthCallback)
    }

    val phoneAuthCallback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
        override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
            phoneAuthCredential?.let {
                addPhoneNumber(phoneAuthCredential)
            }
        }

        override fun onVerificationFailed(exception: FirebaseException) {
            hieLoaderActivity()
            Toast.makeText(this@SigninActivity, "${exception.message}", Toast.LENGTH_SHORT).show()
            exception.printStackTrace()
        }

        override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
            super.onCodeSent(verificationId, token)
            signinLayout.visibility = View.GONE
            toolbar.visibility = View.VISIBLE
            hieLoaderActivity()
            verificationedId = verificationId
            sendOtpLayout.visibility = View.VISIBLE
        }
    }

    private fun addPhoneNumber(phoneAuthCredential: PhoneAuthCredential) {
        firebaseAuth.signInWithCredential(phoneAuthCredential)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    AppGlobal.mobile_no = phoneNumber.text.toString().trim()
                    val otp = phoneAuthCredential.smsCode.toString()
                    checkUserAllReadyRegisterOrNot()
                }
                else{
                    hieLoaderActivity()
                    Toast.makeText(this, "${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    fun closeKeyboard(view: View) {
        val view = this.currentFocus
        if (view!= null){
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken,0)
        }else{
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        }
    }
    private fun showAlertDialog(){
        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(this)
        alertDialog.setTitle("Connection Problem")
        alertDialog.setMessage("Please check your Connection!")
        alertDialog.setIcon(R.drawable.ic_baseline_warning)
        alertDialog.setPositiveButton("Try Again"){ dialog, id ->

            if (isOnline()){
                displayLoaderActivity()
                sendPhoneNumberVerificationCode()
                Toast.makeText(this,"You are Online!", Toast.LENGTH_SHORT).show()
            }else{
                showAlertDialog()
                Toast.makeText(this,"Please Go Online!", Toast.LENGTH_SHORT).show()
            }
        }
        alertDialog.setNegativeButton("Cancel"){dialog, id ->
            dialog.dismiss()
            this.finish()
        }

        val alert = alertDialog.create()
        alert.setCanceledOnTouchOutside(false)
        alert.show()
        val negativeButton = alert.getButton(DialogInterface.BUTTON_NEGATIVE)
        negativeButton.setTextColor(Color.RED)

        val positiveButton = alert.getButton(DialogInterface.BUTTON_POSITIVE)
        positiveButton.setTextColor(Color.GREEN)
    }

    private fun checkUserAllReadyRegisterOrNot(){
        val number = phoneNumber.text.toString().trim()
        firestore.collection("Users").whereEqualTo("phone", number)
            .get()
            .addOnCompleteListener {
                val result = it.result.documents
                if (result.size == 0){

                    showRegisterActivity()
                }
                else{
                    val firstName = result?.get(0)?.data?.getValue("first_name").toString()
                    val lastName = result?.get(0)?.data?.getValue("last_name").toString()
                    if (firstName != "" && lastName != ""){
                        val shortName = result?.get(0)?.data?.getValue("first_name").toString().get(0).toString() + result?.get(0)?.data?.getValue("last_name").toString().get(0).toString()
                        val fullName = firstName + " " + lastName
                        AppGlobal.userShortName = shortName
                        AppGlobal.userFullName = fullName

                    }else{
                        AppGlobal.userShortName = "Hi"
                        AppGlobal.userFullName = "Welcome"
                    }
                    val emailId =  result.get(0)?.data?.getValue("email").toString()
                    AppGlobal.loyaltyPoint = result.get(0)?.data?.getValue("points").toString()
                    AppGlobal.email = emailId
                    AppGlobal.mobile_no = number
                    showDashboard()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
            }
    }

    private fun showRegisterActivity() {
        val intent = Intent(this,RegisterActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
    }

    private fun showDashboard() {
        AppGlobal.userid = firebaseAuth.currentUser?.uid.toString()
        val intent = Intent(this,MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
    }
}