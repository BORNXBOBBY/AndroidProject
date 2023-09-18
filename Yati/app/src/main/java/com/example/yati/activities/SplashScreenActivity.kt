package com.example.yati.activities

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.yati.R
import com.example.yati.global.AppGlobal
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SplashScreenActivity :YatiActivity() {

    private lateinit var diaLog:AlertDialog
    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth : FirebaseAuth
    private var Tag ="splace"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        detectReferLink()

        if (isOnline()) {
            checkForToken()
            checkForFav()
        }
        else{
            showAlert()
            Toast.makeText(this,"Please Go Online!",Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkForToken(){
           if (auth.currentUser != null ){
            loadCurrentUser()
        }else{
            showLogin()
        }
    }

    private fun checkForFav() {
        var favPreferences = this.getSharedPreferences("favPref", Context.MODE_PRIVATE)
        AppGlobal.prefValues = favPreferences
    }

    private fun showLogin() {
        startActivity(Intent(this,SigninActivity::class.java))
        finish()
    }

    private fun showDashboard() {
        startActivity(Intent(this,MainActivity::class.java))
        finish()
    }

    private fun loadCurrentUser() {
        val documentId = auth.currentUser?.uid
        firestore.collection("Users").document(documentId.toString())
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val result = it.result.data
                    currentUserDetails(result)
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Something went wrong please try later.", Toast.LENGTH_SHORT).show()
            }
    }

    private fun currentUserDetails(result: Map<String, Any>?) {
        val firstName = result?.getValue("first_name")
        val lastName = result?.getValue("last_name")

        if (firstName != "" && lastName != ""){
            val shortName = result?.getValue("first_name").toString().get(0).toString() + result?.getValue("last_name").toString().get(0).toString()
            val fullName = result?.getValue("first_name").toString() + " " + result?.getValue("last_name").toString()
            AppGlobal.userShortName = shortName
            AppGlobal.userFullName = fullName

        }else{
                AppGlobal.userShortName = "Hi"
                AppGlobal.userFullName = "Welcome"
        }

        AppGlobal.mobile_no = result?.getValue("phone").toString()
        AppGlobal.email = result?.getValue("email").toString()
        AppGlobal.userid = auth.currentUser?.uid
        if (result?.getValue("points").toString().isNullOrEmpty()) {
            AppGlobal.loyaltyPoint = "0"
        } else {
            AppGlobal.loyaltyPoint = result?.getValue("points").toString()
        }

        showDashboard()
        //loadGenericUserActivities()
    }

    private fun showAlert() {
        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(this)
        alertDialog.setTitle("Check Connection!")
        alertDialog.setIcon(R.drawable.ic_baseline_warning)
        alertDialog.setPositiveButton("Try Again") { dialog, id ->

            if (isOnline()) {
                checkForToken()

                Toast.makeText(this, "You are Online!", Toast.LENGTH_SHORT).show()
            } else {
                showAlert()
                Toast.makeText(this, "Please Go Online!", Toast.LENGTH_SHORT).show()
            }
        }
        alertDialog.setNegativeButton("Cancel") { dialog, id ->
            finish()
        }

        val alert = alertDialog.create()
        alert.setCanceledOnTouchOutside(false)
        alert.show()
        val negativeButton = alert.getButton(DialogInterface.BUTTON_NEGATIVE)
        negativeButton.setTextColor(Color.RED)

        val positiveButton = alert.getButton(DialogInterface.BUTTON_POSITIVE)
        positiveButton.setTextColor(Color.GREEN)
    }

    private fun detectReferLink(){
        Firebase.dynamicLinks
            .getDynamicLink(intent)
            .addOnSuccessListener(this) { pendingDynamicLinkData ->
                // Get deep link from result (may be null if no link is found)
                var deepLink: Uri? = null
                if (pendingDynamicLinkData != null) {
                    deepLink = pendingDynamicLinkData.link
                    val linkArray =deepLink.toString().split("/").toTypedArray()
                    val linkSize=linkArray.size // 7
                    val referralUserId = linkArray.get(6)
                    AppGlobal.referralUserId = referralUserId
                    Log.e(Tag," My refer link " + deepLink.toString())
                    Log.e(Tag," Referral user id " + linkArray.get(6))
                }
            }
            .addOnFailureListener(this) { e -> Log.w(Tag, "getDynamicLink:onFailure", e) }
    }
}