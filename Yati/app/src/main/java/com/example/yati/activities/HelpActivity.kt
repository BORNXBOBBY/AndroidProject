package com.example.yati.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.yati.R
import kotlinx.android.synthetic.main.activity_help.*

class HelpActivity : AppCompatActivity() {
    lateinit var mob :TextView
    var REQUEST_PHONE_CALL= 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help)
        val emailTextView = findViewById<TextView>(R.id.email_textView)
         mob = findViewById(R.id.number_text)
         makeCall_button.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CALL_PHONE),REQUEST_PHONE_CALL)
            }else{
                makeCall()
            }
        }
        email_textView.setOnClickListener {
//            val intent = Intent(Intent.ACTION_SENDTO);
//            intent.setType("text/html");
//            intent.setData(Uri.parse(emailTextView.text.toString().trim()))
//            startActivity(Intent.createChooser(intent, "Send Email"))
        }
    }
    private fun makeCall(){
        val numberText = mob.text.toString()
        val intent = Intent(Intent.ACTION_CALL)
        intent.setData(Uri.parse("tel:$numberText"))
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(this,"Permission denied",Toast.LENGTH_LONG).show()
            return
        }
        startActivity(intent)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == REQUEST_PHONE_CALL){
            makeCall()
        }
    }
}