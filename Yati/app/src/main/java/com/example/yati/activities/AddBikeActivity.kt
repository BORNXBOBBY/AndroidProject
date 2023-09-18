package com.example.yati.activities

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.graphics.Color
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.yati.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_add_bike.*

class AddBikeActivity : YatiActivity() {
    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    lateinit var  datePicker :TextView
    lateinit var  bikeName :EditText
    lateinit var  registerNo :EditText

    var productId: String = ""
    var productName: String = ""
    var productImage: String = ""
    private var selectedDate = ""

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_bike)

        firestore = FirebaseFirestore.getInstance()
        auth =  FirebaseAuth.getInstance()

        productId = intent.getStringExtra("productId").toString()
        productName = intent.getStringExtra("productName").toString()
        productImage =intent.getStringExtra("productImage").toString()

        bikeName = findViewById(R.id.editText_bikeName)
        bikeName.isEnabled = false
        bikeName.setText(productName)

        registerNo = findViewById(R.id.editText_registerNo)
        datePicker = findViewById(R.id.datePicker_textView)
        datePicker.setOnClickListener {
            setDate()

        }
        val bookButton = findViewById<Button>(R.id.button_AddBike)
        bookButton.setOnClickListener {
            val register = registerNo.text.toString().trim()
            if (register.isEmpty()){
                registerNo.error = "Enter register number!"
                registerNo.requestFocus()
                return@setOnClickListener
            }
            val date = datePicker.text.toString().trim()
            if (TextUtils.isEmpty(date)){
                datePicker.error = "Please select a date!"
                datePicker.requestFocus()
                return@setOnClickListener
            }

            BookingAlert()
        }
    }

    private fun loadAddBikes(){
        val addBikeRequest : MutableMap<String, Any> = HashMap()
        addBikeRequest["bike_name"] = bikeName.text.toString()
        addBikeRequest["body"] = "This is my Bike"
        addBikeRequest["bike_image"]  = productImage
        addBikeRequest["purchase_date"] = datePicker.text.toString()
        addBikeRequest["registration_number"] = registerNo.text.toString()
        addBikeRequest["product_id"] = productId

        firestore.collection("Users").document(auth.currentUser?.uid.toString()).collection("My Bikes")
            .add(addBikeRequest)
            .addOnCompleteListener {
                hieLoaderActivity()
                Toast.makeText(this,"Your bike is added",Toast.LENGTH_SHORT).show()
                this.finish()
            }
            .addOnFailureListener {
                hieLoaderActivity()
                Toast.makeText(this,"Not able to add, please try after sometimes",Toast.LENGTH_SHORT).show()
                this.finish()
            }
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.N)
    private fun setDate(){
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            val month = monthOfYear+1
            if (10.compareTo(month) > 0){
                if (10.compareTo(dayOfMonth) > 0){
                    selectedDate = "$year-0$month-0$dayOfMonth"
                }else {
                    selectedDate = "$year-0$month-$dayOfMonth"
                }
            }else{
                if (10.compareTo(dayOfMonth) > 0){
                    selectedDate = "$year-$month-0$dayOfMonth"
                }else {
                    selectedDate = "$year-$month-$dayOfMonth"
                }
            }
            datePicker.setText(selectedDate)
        }, year, month, day)
        dpd.datePicker.maxDate = c.timeInMillis

        dpd.show()
    }

    private fun BookingAlert(){
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Add Your Bike")
        alertDialog.setMessage("Do you want to add this bike ?")
        alertDialog.setPositiveButton("Yes"){_,_ ->
            if (isOnline()){

            displayLoaderActivity()
            loadAddBikes()

            }
            else{
                showAlertDialog()
            }
        }
        alertDialog.setNegativeButton("No"){alert,_ ->
            alert.cancel()
        }

        val alert = alertDialog.create()
        alert.show()
        alert.setCanceledOnTouchOutside(false)

        val negativeButton = alert. getButton(DialogInterface.BUTTON_NEGATIVE)
        negativeButton.setTextColor(Color.RED)

        val positiveButton = alert.getButton(DialogInterface.BUTTON_POSITIVE)
        positiveButton.setTextColor(Color.GREEN)
    }
    fun showAlertDialog(){
        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(this)
        alertDialog.setTitle("Connection Problem")
        alertDialog.setMessage("Please check your Connection!")
        alertDialog.setIcon(R.drawable.ic_baseline_warning)
        alertDialog.setPositiveButton("Try Again"){ dialog, id ->

            if (isOnline()){
                displayLoaderActivity()
                BookingAlert()
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
}