package com.example.yati.activities

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
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
import com.google.firebase.firestore.FirebaseFirestore

class BookServiceActivity : YatiActivity() {
    private lateinit var firestore: FirebaseFirestore
    var productId:String = ""
    var productName:String = ""
    private var selectedDate = ""
    private var documentId = ""
    private var date_textView: TextView? = null
    private var time_textView: TextView? = null
    lateinit var saveButton: Button
    lateinit var instructionEditText: EditText
//    lateinit var checkBox: CheckBox
    lateinit var bikesName_EditText: EditText

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_book_service)
        instructionEditText = findViewById(R.id.instruction_editText)
        saveButton = findViewById(R.id.saveButton_bookService)
        bikesName_EditText = findViewById(R.id.bykeName_editText)
        date_textView = findViewById(R.id.datePicker_field)
        time_textView = findViewById(R.id.timePicker_feild)
//        checkBox = findViewById(R.id.pickUp_checkBox)

        if (intent.hasExtra("bikeId")) {
            productId = intent.getStringExtra("bikeId").toString()
            productName = intent.getStringExtra("bikeName").toString()

            bikesName_EditText.isEnabled = false
            bikesName_EditText.setText(productName)
        }

        // for select date and time
        //date_textView!!.text = "Select Date"
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        date_textView!!.setOnClickListener {
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

                date_textView!!.setText(selectedDate)
            }, year, month, day)
            dpd.datePicker.minDate = c.timeInMillis
            dpd.show()
        }

        time_textView!!.setOnClickListener {
            val cal = Calendar.getInstance()
            val timePickerDialog = TimePickerDialog.OnTimeSetListener{ view, hourOfDay, minute ->
                cal.set(Calendar.HOUR_OF_DAY,hourOfDay)
                cal.set(Calendar.MINUTE,minute)
                time_textView!!.text = SimpleDateFormat("HH:mm").format(cal.time)
            }
            TimePickerDialog(this,timePickerDialog,cal.get(Calendar.HOUR_OF_DAY),cal.get(Calendar.MINUTE),false).show()

        }
        val  typeService_spinner = findViewById<Spinner>(R.id.spinner_typeService)
        val typeService = arrayOf("Free","Paid")
        val arrayAdapter = ArrayAdapter(this,R.layout.spinner_layout,typeService)

//        checkBox = findViewById(R.id.pickUp_checkBox)
        typeService_spinner.adapter = arrayAdapter
        typeService_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
//                checkBox.isVisible = position != 0
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
//                checkBox.isVisible = false
            }

        }

        saveButton.setOnClickListener {

            val bikeName = bikesName_EditText.text.toString().trim()
            val instruction = instructionEditText.text.toString().trim()

            if (bikeName.isEmpty()){
                bikesName_EditText.error = "Please Enter Bike Name!"
                bikesName_EditText.requestFocus()
                return@setOnClickListener
            }

            if (instruction.isEmpty()){
                instructionEditText.error = "Enter Your Instruction!"
                instructionEditText.requestFocus()
                return@setOnClickListener
            }

            BookingAlert()
        }

    }

    private fun loadBookService(){
        var dataStr = date_textView?.text.toString().trim()
        var timeStr = time_textView?.text.toString().trim()
        val dateTimeVal: String = dataStr + "T" + timeStr

        firestore = FirebaseFirestore.getInstance()
        val bookingRequest : MutableMap<String, Any> = HashMap()
        bookingRequest["bike_name"] = bikesName_EditText.text.toString().trim()
        bookingRequest["problem_in_bike"] = instructionEditText.text.toString().trim()
        bookingRequest["requested_date_time"] = dateTimeVal
        bookingRequest["arrived_date_time"] = ""
        bookingRequest["delivered_date_time"] = ""
        bookingRequest["status"] = "Requested"
        bookingRequest["user_id"] = AppGlobal.userid.toString()
        bookingRequest["user_name"] = AppGlobal.userFullName.toString()
        bookingRequest["email"] = AppGlobal.email.toString()
        bookingRequest["rating"] = 0
        bookingRequest["id"] = ""
        bookingRequest["feedback"] = ""
        firestore.collection("Services")
            .add(bookingRequest)
            .addOnCompleteListener {
                documentId =it.result.id
                updateDocumentId()
            }
            .addOnFailureListener {
                hieLoaderActivity()
                Toast.makeText(this, "Booking failed", Toast.LENGTH_SHORT).show()
            }
    }
    private fun updateDocumentId(){
        val updateRequest: MutableMap<String,Any> = HashMap()
        updateRequest["id"] = documentId
        firestore.collection("Services").document(documentId).update(updateRequest)
            .addOnCompleteListener {
                hieLoaderActivity()
                onBackPressed()
                Toast.makeText(this, "Booking successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                hieLoaderActivity()
                Toast.makeText(this, "Booking failed", Toast.LENGTH_SHORT).show()
            }
    }

    private fun BookingAlert(){
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Service Booking")
        alertDialog.setMessage("Do you want to book this Service ?")
        alertDialog.setPositiveButton("Yes"){_,_ ->
            if (isOnline()) {

                displayLoaderActivity()
                loadBookService()

            }else{
                Toast.makeText(this, "You are offline", Toast.LENGTH_SHORT).show()
                showAlertDialog()
            }
        }
        alertDialog.setNegativeButton("No"){alert,_ ->
            alert.cancel()
        }

        val alert = alertDialog.create()
        alert.show()
        alert.setCanceledOnTouchOutside(false)

        val negativeButton = alert.getButton(DialogInterface.BUTTON_NEGATIVE)
        negativeButton.setTextColor(Color.RED)

        val positiveButton = alert.getButton(DialogInterface.BUTTON_POSITIVE)
        positiveButton.setTextColor(Color.GREEN)
    }

    fun keyboardClose(view: View) {
        val view = this.currentFocus
        if (view!= null){
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken,0)
        }else{
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        }
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