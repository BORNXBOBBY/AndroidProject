package com.example.sqlitecrudandroid

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class UpdateDataActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_data)

        val upDateBtn=findViewById<Button>(R.id.upDateData)

        val id=findViewById<EditText>(R.id.idUpDAte)
        val upDateName=findViewById<EditText>(R.id.upDateName)
        val upDateEmail=findViewById<EditText>(R.id.upDateEmail)
        val upDateUserId=findViewById<EditText>(R.id.upDateUserId)
        val upDatePassword=findViewById<EditText>(R.id.upDatePassword)
        val upDateContact=findViewById<EditText>(R.id.upDateContact)

        upDateBtn.setOnClickListener {

            val db = DataBaseHelper(this, null)
            val id=id.text.toString()
            val upDateNameData = upDateName.text.toString()
            val upDateEmailData = upDateEmail.text.toString()
            val upDateUserData = upDateUserId.text.toString()
            val upDatePasswordData = upDatePassword.text.toString()
            val upDateContactData = upDateContact.text.toString()
            db.updateData(id,upDateNameData, upDateEmailData, upDateUserData, upDatePasswordData, upDateContactData)

            Toast.makeText(this, upDateNameData + upDateEmailData+upDateUserData+upDatePasswordData+upDateContactData+ " Added To DataBase", Toast.LENGTH_LONG).show()

            startActivity(Intent(this,ViewDataActivity::class.java))

        }


    }
}