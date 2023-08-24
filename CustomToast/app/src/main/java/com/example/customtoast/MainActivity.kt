package com.example.customtoast

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        val btn_show_toast = findViewById<Button>(R.id.btn_show_toast)
        // apply an onClickListener() method
        btn_show_toast.setOnClickListener{
                Toast(this).showCustomToast ("Hello! This is a custom Toast!", this)
        }
    }
}