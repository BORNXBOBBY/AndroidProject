package com.example.sqliterecyclerviewdata

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val save=findViewById<Button>(R.id.save)


        val name=findViewById<EditText>(R.id.name)
        val email=findViewById<EditText>(R.id.email)
        val userId=findViewById<EditText>(R.id.userid)
        val password=findViewById<EditText>(R.id.password)
        val contactTxt=findViewById<EditText>(R.id.contact)


        save.setOnClickListener {

            val db = DataBaseHelper(this, null)

            val nameData = name.text.toString()
            val emailData = email.text.toString()
            val userData = userId.text.toString()
            val passwordData = password.text.toString()
            val contactData = contactTxt.text.toString()

            db.insertData (nameData,emailData, userData, passwordData, contactData)

            Toast.makeText(this,
                "$nameData$emailData$userData$passwordData Added To DataBase", Toast.LENGTH_LONG).show()

            name.text.clear()
            email.text.clear()
            userId.text.clear()
            password.text.clear()
            contactTxt.text.clear()
            startActivity(Intent(this,ViewDataActivity::class.java))
        }


    }
}