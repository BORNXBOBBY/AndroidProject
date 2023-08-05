package com.example.passworddifferentcharacter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputFilter
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

      var editText = findViewById<EditText>(R.id.edtText);
        var btnCheck = findViewById<Button>(R.id.btnCheck);

        btnCheck.setOnClickListener {

                if (isValidPassword(editText.getText().toString().trim())) {
                    Toast.makeText(this, "Valid", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "InValid", Toast.LENGTH_SHORT).show();
                }
            }
        val maxLength = 8
        val filterArray = arrayOf<InputFilter>(InputFilter.LengthFilter(maxLength))
        editText.filters = filterArray

        };

    }
    fun isValidPassword(password: String?) : Boolean {
        password?.let {
            val passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$"
            val passwordMatcher = Regex(passwordPattern)

            return passwordMatcher.find(password) != null
        } ?: return false



    }
