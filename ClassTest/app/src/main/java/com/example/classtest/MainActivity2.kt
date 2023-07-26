package com.example.classtest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog


class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        val name =findViewById<TextView>(R.id.name)
        val age =findViewById<TextView>(R.id.age)
        val email =findViewById<TextView>(R.id.email)
        val adhar =findViewById<EditText>(R.id.adhar)
        val button =findViewById<Button>(R.id.submit2)

        val names =intent.getStringExtra("a")
        name.text=names

        val ages =intent.getStringExtra("b")
        age.text=ages

        val emails =intent.getStringExtra("c")
        email.text=emails



            button.setOnClickListener {
            var number=adhar.text.toString()
            val alertDialog = AlertDialog.Builder(this)
            alertDialog.setTitle("This Message has been destroyed")
            alertDialog.setMessage(number)
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