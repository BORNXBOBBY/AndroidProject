package com.example.alert_function

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        val button = findViewById<Button>(R.id.clear_button)

        val name=findViewById<EditText>(R.id.name)
        val age=findViewById<EditText>(R.id.age)

        val sharedPreferences=getSharedPreferences("mySharedPreferences", Context.MODE_PRIVATE)
        val sharepref = sharedPreferences.edit()

        button.setOnClickListener {
            sharepref.putString("name",name.text.toString()).commit()
            sharepref.putString("age",age.text.toString()).commit()
            startActivity(Intent(this,MainActivity::class.java))
        }
    }
}