package com.example.yati.activities

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.RequiresApi
import com.example.yati.R
import com.example.yati.global.AppGlobal
import com.example.yati.models.*
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*
import java.util.regex.Pattern
import kotlin.collections.HashMap

class RegisterActivity : YatiActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var phoneAuth : PhoneAuthProvider
    private var verificationedId: String? = null
    private var uid = ""
    private var referralUserPoint:Int = 0
    private var expiryDate = ""
    private var dateForPoint = ""
    lateinit var firstNameEditText: EditText
    lateinit var lastNameEditText: EditText
    lateinit var emailEditText: EditText
    lateinit var registerLayout: LinearLayout
    lateinit var registerButton : Button
    private  val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

         firebaseAuth = FirebaseAuth.getInstance()
         firestore = FirebaseFirestore.getInstance()
         phoneAuth = PhoneAuthProvider.getInstance()


         firstNameEditText = findViewById(R.id.register_firstName)
         lastNameEditText = findViewById(R.id.register_lastName)
         emailEditText = findViewById(R.id.register_email)
         registerLayout = findViewById(R.id.register_layout)
        AppGlobal.userid = firebaseAuth.currentUser?.uid.toString()
        AppGlobal.email = emailEditText.text.toString().trim()
        AppGlobal.userFullName = firstNameEditText.text.toString() + lastNameEditText.text.toString().trim()
//        AppGlobal.userShortName = firstNameEditText.text[0].toString() + lastNameEditText.text[0].toString().trim()

        val skipText = findViewById<TextView>(R.id.skip_text)
        skipText.setOnClickListener {
            showDashbord()
        }
        val calender = Calendar.getInstance()

        val year = calender.get(Calendar.YEAR) + 1
        val pyear = calender.get(Calendar.YEAR) + 1
        val month = calender.get(Calendar.MONTH) + 1
        val day = calender.get(Calendar.DAY_OF_MONTH)

        if (10.compareTo(month) > 0){
            if (10.compareTo(day) > 0){
                expiryDate = "$year-0$month-0$day"
                dateForPoint = "$pyear-0$month-0$day"
            }else {
                expiryDate = "$year-0$month-$day"
                dateForPoint = "$pyear-0$month-$day"
            }
        }else{
            if (10.compareTo(day) > 0){
                expiryDate = "$year-$month-0$day"
                dateForPoint = "$pyear-$month-0$day"
            }else {
                expiryDate = "$year-$month-$day"
                dateForPoint = "$pyear-$month-$day"
            }
        }


        registerButton = findViewById(R.id.register_Button)
        registerButton.setOnClickListener {
            val firstName =firstNameEditText.text.toString()
            val lastName =lastNameEditText.text.toString()

            if (firstName.isEmpty()) {
                firstNameEditText.error = "Enter First Name"
                firstNameEditText.requestFocus()
                return@setOnClickListener
            }

            if (lastName.isEmpty()) {
                lastNameEditText.error = "Enter Last Name"
                lastNameEditText.requestFocus()
                return@setOnClickListener
            }

            if(isOnline()){
                displayLoaderActivity()
                updateUserDetails()
            }else{
                showAlertDialog()
            }
        }

        storeUserDetails()
    }

    private fun mobileValidate(numberText: String): Boolean {

        var pattern = Pattern.compile("[6-9][0-9]{9}")
        var matcher = pattern.matcher(numberText)
        return matcher.matches()
    }

    private fun storeUserDetails() {
         uid = firebaseAuth.currentUser?.uid.toString()
            val user : MutableMap<String, Any> = HashMap()
                  user["first_name"] = ""
                  user["last_name"] = ""
                  user["email"] = ""
                  user["password"] = ""
                  user["phone"] = AppGlobal.mobile_no.toString()
                  user["points"] = 100
                  user["birth_day"] = " "
                  user["gender"] = " "
                  user["profile_image"] = " "
                  user["uid"] = uid
        firestore.collection("Users").document(uid)
            .set(user)
            .addOnSuccessListener(OnSuccessListener {
                AppGlobal.loyaltyPoint = "100"
                //Toast.makeText(this, "Details is added", Toast.LENGTH_SHORT).show()
                checkReferralUser()
            })
            .addOnFailureListener(OnFailureListener {
                hieLoaderActivity()
            })
    }

    private fun updateUserDetails(){
        val fName= firstNameEditText.text.toString().trim()
        val lName= lastNameEditText.text.toString().trim()
        val email= emailEditText.text.toString().trim()
        uid = firebaseAuth.currentUser?.uid.toString()
        val user : MutableMap<String, Any> = HashMap()
        user["first_name"] = fName
        user["last_name"] = lName
        user["email"] = email

        firestore.collection("Users").document(uid)
            .update(user)
            .addOnSuccessListener(OnSuccessListener {

                AppGlobal.email = email
                AppGlobal.loyaltyPoint = "100"
                AppGlobal.userShortName = fName.get(0).toString() + lName.get(0).toString()
                AppGlobal.userFullName = fName + lName
                Toast.makeText(this, "Your details is updated", Toast.LENGTH_SHORT).show()
                showDashbord()
            })
            .addOnFailureListener(OnFailureListener {
                hieLoaderActivity()
                Toast.makeText(this, "Details adding failed", Toast.LENGTH_SHORT).show()
            })
    }

    private fun checkReferralUser() {
        displayLoaderActivity()
        if (AppGlobal.referralUserId == "") {
                addFirstCouponInUserActivities()
        }
        else{
                loadReferralUser(AppGlobal.referralUserId.toString())
       }
    }

    private fun isEmailValide():Boolean {
        val email =emailEditText.text.toString().trim()
        return email.matches(emailPattern.toRegex())
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
                updateUserDetails()
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

    private fun loadReferralUser(referralUid :String){
        firestore.collection("Users").document(referralUid).get()
            .addOnSuccessListener {
                val point:Number  = it.get("points") as Number
                referralUserPoint = point.toInt() + 100
                loadReferralUserPoints(referralUserPoint)
            }
            .addOnFailureListener {
                it.printStackTrace()
            }
    }
    @SuppressLint("CheckResult")
    private fun loadReferralUserPoints(referralUserPoints: Int){

            val pointUpdateRequest: MutableMap<String, Any> = HashMap()
            pointUpdateRequest["points"] = referralUserPoints

            firestore.collection("Users").document(AppGlobal.referralUserId.toString()).update(pointUpdateRequest)
                .addOnSuccessListener {
                    hieLoaderActivity()
//                    Toast.makeText(this, "Registration successfully", Toast.LENGTH_SHORT).show()
                    addFirstCouponInUserActivities()
                }
                .addOnFailureListener {
                    hieLoaderActivity()
                    Toast.makeText(this, "Registration successfully", Toast.LENGTH_SHORT).show()
                    showDashbord()
                }
    }

    private fun addFirstCouponInUserActivities(){
        val userActivityRequest = HomeRecords(AppGlobal.userid, "Coupon", "Free Cleaning", "", "Coupon" , expiryDate, "", "")
        firestore.collection("Users Activities")
            .add(userActivityRequest)
            .addOnSuccessListener {
                addFirstCoupon()
            }
            .addOnFailureListener {
                hieLoaderActivity()
                showDashbord()
            }
    }

    private fun addFirstCoupon () {
        val addFirstCouponRequest = CouponsModels("Free Cleaning", AppGlobal.userid, expiryDate + "T00:00" , "Active" , "Free" , "", "")
        firestore.collection("Coupons")
            .add(addFirstCouponRequest)
            .addOnSuccessListener {
                addFirstPointInUserActivities()
            }
            .addOnFailureListener {
                hieLoaderActivity()
                showDashbord()
            }
    }
    private fun addFirstPointInUserActivities(){
        val userActivityRequest = HomeRecords(AppGlobal.userid, "Points", "Your welcome points", "", "Bonus" , AppGlobal.getTodayDate()+"T00:00", "100", "")

        firestore.collection("Users Activities")
            .add(userActivityRequest)
            .addOnSuccessListener {
                addFirstPoint()
            }
            .addOnFailureListener {
                hieLoaderActivity()
                showDashbord()
            }
    }
    private fun addFirstPoint(){
        val userActivityRequest: MutableMap<String, Any> = HashMap()
            userActivityRequest["user_id"] = AppGlobal.userid.toString()
            userActivityRequest["title"] ="Points"
            userActivityRequest["description"] ="Your Welcome Points"
            userActivityRequest["points"] = 100
            userActivityRequest["transaction_number"] = 1032278120
            userActivityRequest["transaction_type"] ="Bonus"
            userActivityRequest["date"] = AppGlobal.getTodayDate()+"T00:00"

            firestore.collection("Points")
            .add(userActivityRequest)
            .addOnSuccessListener {
                hieLoaderActivity()

            }
            .addOnFailureListener {
                hieLoaderActivity()

            }
    }

    private fun showDashbord() {
        val intent = Intent(this,MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
    }
}


