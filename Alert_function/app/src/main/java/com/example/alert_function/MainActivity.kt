package com.example.alert_function

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener {
            val alertDialog = AlertDialog.Builder(this)
            alertDialog.setTitle("This Message has been destroyed")
            alertDialog.setMessage("Are you sure you want to destroy this Message?")
            alertDialog.setIcon(R.drawable.baseline_home_24)
            alertDialog.setView(R.layout.alert)

            alertDialog.setPositiveButton("delete") { dialog, which ->
                Toast.makeText(this, "This message has been deleted", Toast.LENGTH_SHORT).show()
            }
            alertDialog.setNegativeButton("cancel") { dialog, which ->
                Toast.makeText(this, "cancel", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
            alertDialog.create()
            alertDialog.show()
        }
    }
}