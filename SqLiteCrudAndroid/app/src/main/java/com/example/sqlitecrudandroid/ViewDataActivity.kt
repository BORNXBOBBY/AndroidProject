package com.example.sqlitecrudandroid

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class ViewDataActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId", "Range", "SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_data)
        val iD=findViewById<TextView>(R.id.id)
        val nameView=findViewById<TextView>(R.id.NameView)
        val emailView=findViewById<TextView>(R.id.emailView)
        val userId=findViewById<TextView>(R.id.userIdView)
        val password=findViewById<TextView>(R.id.passwordView)
        val contact=findViewById<TextView>(R.id.contactView)
        val view=findViewById<Button>(R.id.view)
        val updateDataBtn=findViewById<Button>(R.id.updateDataBtn)
        val deleteData=findViewById<Button>(R.id.deleteData)
        val addNewData=findViewById<Button>(R.id.add)

        addNewData.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
        }

        deleteData.setOnClickListener {
            startActivity(Intent(this,DeleteDataActivity::class.java))
        }

        updateDataBtn.setOnClickListener {
            startActivity(Intent(this,UpdateDataActivity::class.java))
        }


        view.setOnClickListener{

            val db = DataBaseHelper(this, null)


            val cursor = db.viewData()

            cursor!!.moveToFirst()
            iD.append(cursor.getString(cursor.getColumnIndex(DataBaseHelper.ID_COL))+"\n")
            nameView.append(cursor.getString(cursor.getColumnIndex(DataBaseHelper.NAME_COl)) + "\n")
            emailView.append(cursor.getString(cursor.getColumnIndex(DataBaseHelper.EMAIL_COL)) + "\n")
            userId.append(cursor.getString(cursor.getColumnIndex(DataBaseHelper.USER_COL)) + "\n")
            password.append(cursor.getString(cursor.getColumnIndex(DataBaseHelper.PASS_COL)) + "\n")
            contact.append(cursor.getString(cursor.getColumnIndex(DataBaseHelper.CONTACT_COL)) + "\n")

            // moving our cursor to next
            // position and appending values
            while(cursor.moveToNext()){
                iD.append(cursor.getString(cursor.getColumnIndex(DataBaseHelper.ID_COL))+"\n")
                nameView.append(cursor.getString(cursor.getColumnIndex(DataBaseHelper.NAME_COl)) + "\n")
                emailView.append(cursor.getString(cursor.getColumnIndex(DataBaseHelper.EMAIL_COL)) + "\n")
                userId.append(cursor.getString(cursor.getColumnIndex(DataBaseHelper.USER_COL)) + "\n")
                password.append(cursor.getString(cursor.getColumnIndex(DataBaseHelper.PASS_COL)) + "\n")
                contact.append(cursor.getString(cursor.getColumnIndex(DataBaseHelper.CONTACT_COL)) + "\n")

            }

            // at last we close our cursor
            cursor.close()
        }
    }
}