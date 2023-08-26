package com.example.sqliteandroid

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val enterName = findViewById<EditText>(R.id.enter_name)
        val enterEmail = findViewById<EditText>(R.id.enter_email)
        val enterUserId = findViewById<EditText>(R.id.enter_userid)
        val enterPass = findViewById<EditText>(R.id.enter_pass)
        val enterContact = findViewById<EditText>(R.id.enter_contact)

        val addName = findViewById<Button>(R.id.addName)

        val printName = findViewById<Button>(R.id.printName)

        val getName = findViewById<TextView>(R.id.name)
        val getEmail = findViewById<TextView>(R.id.email)
        val getUserId = findViewById<TextView>(R.id.userid)
        val getPass = findViewById<TextView>(R.id.pass)
        val getContact = findViewById<TextView>(R.id.contact)


        addName.setOnClickListener{

            val db = DBHelper(this, null)

            val nameData = enterName.text.toString()
            val emailData = enterEmail.text.toString()
            val userData = enterUserId.text.toString()
            val passwordData = enterPass.text.toString()
            val contactData = enterContact.text.toString()

            // calling method to add
            // name to our database
            db.insertData(nameData, emailData, userData, passwordData, contactData)

            // Toast to message on the screen
            Toast.makeText(this, "$nameData added to database", Toast.LENGTH_LONG).show()

            // at last, clearing edit texts
            enterName.text.clear()
            enterEmail.text.clear()
            enterUserId.text.clear()
            enterPass.text.clear()
            enterContact.text.clear()

        }

        printName.setOnClickListener{

            // creating a DBHelper class
            // and passing context to it
            val db = DBHelper(this, null)

            // below is the variable for cursor
            // we have called method to get
            // all names from our database
            // and add to name text view
            val cursor = db.getName()

            // moving the cursor to first position and
            // appending value in the text view
            cursor!!.moveToFirst()
            getName.append(cursor.getString(cursor.getColumnIndex(DBHelper.NAME_COl)) + "\n")
            getEmail.append(cursor.getString(cursor.getColumnIndex(DBHelper.EMAIL_COL)) + "\n")
            getUserId.append(cursor.getString(cursor.getColumnIndex(DBHelper.USERID_COL)) + "\n")
            getPass.append(cursor.getString(cursor.getColumnIndex(DBHelper.PASS_COL)) + "\n")
            getContact.append(cursor.getString(cursor.getColumnIndex(DBHelper.CONTACT_COL)) + "\n")

            // moving our cursor to next
            // position and appending values
            while(cursor.moveToNext() ){
                getName.append(cursor.getString(cursor.getColumnIndex(DBHelper.NAME_COl)) + "\n")
                getEmail.append(cursor.getString(cursor.getColumnIndex(DBHelper.EMAIL_COL)) + "\n")
                getUserId.append(cursor.getString(cursor.getColumnIndex(DBHelper.USERID_COL)) + "\n")
                getPass.append(cursor.getString(cursor.getColumnIndex(DBHelper.PASS_COL)) + "\n")
                getContact.append(cursor.getString(cursor.getColumnIndex(DBHelper.CONTACT_COL)) + "\n")
            }
            // at last we close our cursor
            cursor.close()
        }

    }
}