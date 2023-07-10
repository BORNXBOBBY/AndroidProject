package com.example.alert_function

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.alert_function.R

class MainActivity4 : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main4)
        val Dialog= AlertDialog.Builder(this)
        Dialog.setTitle("Remove user")
        Dialog.setMessage("Are you sure you want to remove user?")
        Dialog.setIcon(R.drawable.baseline_warning_24)
        Dialog.setPositiveButton("Yes"){
                dialog,which->
        }
        val text =findViewById<TextView>(R.id.text)

        text.setOnClickListener {


            text.text= "welcome"
        }
        Dialog.setNegativeButton("No"){
                dialog,which->
        }
        Dialog.create()
        Dialog.show()
    }
}