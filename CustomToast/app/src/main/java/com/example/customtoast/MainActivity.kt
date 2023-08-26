package com.example.customtoast

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        val btn_show_One = findViewById<Button>(R.id.btnUp)
        val btn_show_Two = findViewById<Button>(R.id.btnDown)

        val btn_show_Three = findViewById<Button>(R.id.btnRight)

        val btn_show_Four = findViewById<Button>(R.id.btnLeft)
        val btn_show_Five = findViewById<Button>(R.id.btncenter)

        // apply an onClickListener() method
        btn_show_One.setOnClickListener{
                Toast(this).showToastOne ("Hello! This is a custom Toast!", this)
        }
        btn_show_Two.setOnClickListener{
            Toast(this).showToastTwo ("Hello! This is a custom Toast!", this)
        }
        btn_show_Three.setOnClickListener{
            Toast(this).showToastThree ("Hello! This is a custom Toast!", this)
        }
        btn_show_Four.setOnClickListener{
            Toast(this).showToastFour ("Hello! This is a custom Toast!", this)
        }
        btn_show_Five.setOnClickListener{
            Toast(this).showToastFive ("Hello! This is a custom Toast!", this)
        }


    }
}