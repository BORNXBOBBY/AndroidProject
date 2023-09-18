package com.example.yati.activities

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.example.yati.R
import com.example.yati.global.AppGlobal
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_profile.*
import java.util.*

class ProfileActivity :YatiActivity() {
    private lateinit var auth :FirebaseAuth
    private lateinit var documentId :String
    private lateinit var firestore :FirebaseFirestore

//    private val PERMISSION_CODE = 1000
//    private val IMAGE_CAPTURE_CODE = 1001
//    var image_uri:Uri? = null
    lateinit var firstName :EditText
    lateinit var lastName :EditText
    lateinit var email :EditText
    lateinit var phone :EditText
    lateinit var profileImageText :TextView
    lateinit var birthday:TextView
    lateinit var genderSpinner: Spinner
    lateinit var shortName : String

    private var gender: String = ""

    val genderOptions = arrayOf("Select", "Male", "Female", "Transgender")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
//        registerUser()
//        readData()

        if (isOnline()){
            displayLoaderActivity()
            loadCurrentUser()
        }else{
            showAlertDialog()
        }

        val saveChangeButton = findViewById<Button>(R.id.save_change_profile_button)

        firstName = findViewById(R.id.firstName_editText)
        lastName = findViewById(R.id.lastName_editText)
        phone = findViewById(R.id.phone_edit_text)
        email = findViewById(R.id.email_edit_text)
        profileImageText = findViewById(R.id.userProfile_textView)
        birthday = findViewById(R.id.birthday_textView)
        genderSpinner = findViewById(R.id.gender_spinner)

        resetPassword_text.setOnClickListener { 
            val intent = Intent(this, ForgetPasswordActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }

//        profileImage.setOnClickListener {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
//                if (checkSelfPermission(Manifest.permission.CAMERA)
//                    ==PackageManager.PERMISSION_DENIED ||
//                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                    ==PackageManager.PERMISSION_DENIED ){
//
//                    val permission = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                    requestPermissions(permission, PERMISSION_CODE)
//                }
//                else{
//                    openCamera()
//                }
//            }
//            else{
//                openCamera()
//            }
//        }

        saveChangeButton.setOnClickListener {
            val birthdayText = birthday.text.toString()

            if (TextUtils.isEmpty(birthdayText)) {
                birthday.error = "Enter Birthday"
                birthday.requestFocus()
                return@setOnClickListener
            }
            if (isOnline()) {
                displayLoaderActivity()
                updateUserProfile()
            }else{
                showUpdateAlertDialog()
            }
        }

        var day: Int
        var bMonth: Int
        var year: Int

        birthday_textView.setOnClickListener {

            val calendar = Calendar.getInstance()
            day = calendar.get(Calendar.DAY_OF_MONTH)
            bMonth = calendar.get(Calendar.MONTH)
            year = calendar.get(Calendar.YEAR)

            val datePickerDialog = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    birthday.text.toString().trim()
                    val month = month + 1
                    birthday.setText("$year-$month-$dayOfMonth")

                },
                year,
                bMonth,
                day
            )

            datePickerDialog.show()

        }

        val genderArrayAdapter = ArrayAdapter(this, R.layout.spinner_layout, genderOptions)
        genderSpinner.adapter = genderArrayAdapter
        genderSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                gender = genderOptions[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }

    private fun loadCurrentUser() {
         documentId = auth.currentUser?.uid.toString()
        firestore.collection("Users").document(documentId)
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val result = it.result.data
                    hieLoaderActivity()
                    currentUserDetails(result)
                }
            }
            .addOnFailureListener {
                hieLoaderActivity()
                Toast.makeText(this, "Something went wrong please try later.", Toast.LENGTH_SHORT).show()
            }
    }

    private fun currentUserDetails(result: Map<String, Any>?) {
        val fName = result?.getValue("first_name").toString()
        val lName= result?.getValue("last_name").toString()
        val emailId =  result?.getValue("email").toString()
        val number = result?.getValue("phone").toString()
        if (fName != "" && lName != "") {

            shortName = result?.getValue("first_name").toString().get(0).toString() + result?.getValue("last_name").toString().get(0).toString()
            val fullName = result?.getValue("first_name").toString() + " " + result?.getValue("last_name").toString()
            profileImageText.text = shortName
            AppGlobal.userShortName = shortName
            AppGlobal.userFullName = fullName
        }else{
            profileImageText.text = "Hi"
        }
        AppGlobal.loyaltyPoint = result?.getValue("points").toString()
        AppGlobal.email = emailId
        AppGlobal.mobile_no = number
            val dob =result?.getValue("birth_day")
        if (dob!= null){
            birthday.setText(dob.toString())
        }




        firstName.setText(fName)
        lastName.setText(lName)
        email.setText(emailId)
        phone.setText(number)
        genderSpinner.setSelection(genderOptions.lastIndexOf(result?.getValue("gender").toString()))
    }

    fun updateUserProfile(){
        val user : MutableMap<String, Any> = HashMap()
        user["first_name"] = firstName.text.toString()
        user["last_name"] = lastName.text.toString()
        user["email"] = email.text.toString()
        user["phone"] = phone.text.toString()
        user["gender"] = gender
        user["birth_day"] = birthday.text.toString()

        firestore.collection("Users").document(documentId)
            .update(user)
            .addOnCompleteListener {
                hieLoaderActivity()
                Toast.makeText(this, "Your profile is Updated successfully", Toast.LENGTH_SHORT).show()
                updateSuccess()
            }
            .addOnFailureListener {
                hieLoaderActivity()
                Toast.makeText(this, "Update Failed", Toast.LENGTH_SHORT).show()
            }
    }

    private fun updateSuccess() {
        val saveProfilePicturePref = this.getSharedPreferences("loginPref", Context.MODE_PRIVATE)
        val saveCameraPic =  saveProfilePicturePref.getString("camera_pic_save", "")
        with(saveProfilePicturePref.edit()){
            putString("camera_pic", saveCameraPic.toString())
                .commit()
        }
        Toast.makeText(this, "Update Successfully", Toast.LENGTH_LONG).show()
        onBackPressed()
    }

    fun showAlertDialog(){
        val alertDialog:AlertDialog.Builder = AlertDialog.Builder(this)
        alertDialog.setTitle("Connection Problem")
        alertDialog.setMessage("Please check your Connection!")
        alertDialog.setIcon(R.drawable.ic_baseline_warning)
        alertDialog.setPositiveButton("Try Again"){ dialog, id ->

            if (isOnline()){
                displayLoaderActivity()
                loadCurrentUser()
                Toast.makeText(this, "You are Online!", Toast.LENGTH_SHORT).show()
            }else{
                showAlertDialog()
                Toast.makeText(this, "Please Go Online!", Toast.LENGTH_SHORT).show()
            }
        }
        alertDialog.setNegativeButton("Cancel"){ dialog, id ->
            dialog.dismiss()
        }

        val alert = alertDialog.create()
        alert.setCanceledOnTouchOutside(false)
        alert.show()
        val negativeButton = alert.getButton(DialogInterface.BUTTON_NEGATIVE)
        negativeButton.setTextColor(Color.RED)

        val positiveButton = alert.getButton(DialogInterface.BUTTON_POSITIVE)
        positiveButton.setTextColor(Color.GREEN)
    }
    fun showUpdateAlertDialog(){
        val alertDialog:AlertDialog.Builder = AlertDialog.Builder(this)
        alertDialog.setTitle("Connection Problem")
        alertDialog.setMessage("Please check your Connection!")
        alertDialog.setIcon(R.drawable.ic_baseline_warning)
        alertDialog.setPositiveButton("Try Again"){ dialog, id ->

            if (isOnline()){
                displayLoaderActivity()
                updateUserProfile()
                Toast.makeText(this, "You are Online!", Toast.LENGTH_SHORT).show()
            }else{
                showAlertDialog()
                Toast.makeText(this, "Please Go Online!", Toast.LENGTH_SHORT).show()
            }
        }
        alertDialog.setNegativeButton("Cancel"){ dialog, id ->
            dialog.dismiss()
        }

        val alert = alertDialog.create()
        alert.setCanceledOnTouchOutside(false)
        alert.show()
        val negativeButton = alert.getButton(DialogInterface.BUTTON_NEGATIVE)
        negativeButton.setTextColor(Color.RED)

        val positiveButton = alert.getButton(DialogInterface.BUTTON_POSITIVE)
        positiveButton.setTextColor(Color.GREEN)
    }

    fun closeKeyboard(view: View) {
        val view = this.currentFocus
        if (view!= null){
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }else{
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        }
    }

//    private fun openCamera(){
//        val values = ContentValues()
//        values.put(MediaStore.Images.Media.TITLE,"New Picture")
//        values.put(MediaStore.Images.Media.DESCRIPTION,"From the Camera")
//        image_uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
//
//        val cameraIntent =  Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri)
//       startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE)
//    }
//
//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
//
//        when(requestCode){
//            PERMISSION_CODE ->{
//                if (grantResults.size > 0 && grantResults.get(0)== PackageManager.PERMISSION_GRANTED){
//
//                  openCamera()
//
//                }
//                else{
//                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show()
//                }
//            }
//        }
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//
//        if (resultCode == Activity.RESULT_OK){
//            profileImage.setImageURI(image_uri)
//            val saveProfilePicturePref = this.getSharedPreferences("loginPref", Context.MODE_PRIVATE)
//            with(saveProfilePicturePref.edit()){
//                putString("camera_pic_save", image_uri.toString())
//                    .commit()
//            }
//        }
//    }
}


