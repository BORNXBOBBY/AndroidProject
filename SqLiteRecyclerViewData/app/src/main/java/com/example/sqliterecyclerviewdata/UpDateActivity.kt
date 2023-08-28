package com.example.sqliterecyclerviewdata

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class UpDateActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId", "Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_up_date)

        val upDateData=findViewById<Button>(R.id.upDate)
        val upDateId=findViewById<TextView>(R.id.upDateId)
        val upDateName=findViewById<EditText>(R.id.upDateName)
        val upDateEmail=findViewById<EditText>(R.id.upDateEmail)
        val upDateUserid=findViewById<EditText>(R.id.upDateUserid)
        val upDatePassword=findViewById<EditText>(R.id.upDatePassword)
        val upDateContact=findViewById<EditText>(R.id.upDateContact)

        val deleteData=findViewById<Button>(R.id.deleteData)

        val upDateDta=intent.getStringExtra("id").toString()
        val nameData=intent.getStringExtra("name").toString()
        val emailData=intent.getStringExtra("email").toString()
        val userIdData=intent.getStringExtra("userId").toString()
        val passwordData=intent.getStringExtra("password").toString()
        val contactData=intent.getStringExtra("contact").toString()


        upDateId.setText(upDateDta)
        upDateName.setText(nameData).toString()
        upDateEmail.setText(emailData)
        upDateUserid.setText(userIdData)
        upDatePassword.setText(passwordData)
        upDateContact.setText(contactData)

        upDateData.setOnClickListener {

            val db = DataBaseHelper(this, null)
            val dataId=upDateId.text.toString()
            val dataName = upDateName.text.toString()
            val dataEmail = upDateEmail.text.toString()
            val dataUserId =upDateUserid.text.toString()
            val dataPass = upDatePassword.text.toString()
            val dataContact = upDateContact.text.toString()

            db.updateData(dataId,dataName, dataEmail, dataUserId, dataPass,dataContact)

            Toast.makeText(this, " Added To DataBase", Toast.LENGTH_LONG).show()

            startActivity(Intent(this,ViewDataActivity::class.java))
        }

        deleteData.setOnClickListener {
            val db = DataBaseHelper(this, null)
            val dataId=upDateId.text.toString()
            db.deleteData(dataId)
            Toast.makeText(this,  " DataBase Data Delete", Toast.LENGTH_LONG).show()
            startActivity(Intent(this,ViewDataActivity::class.java))

        }

    }
}