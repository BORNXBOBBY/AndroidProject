package com.example.paymentgatewayandroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import org.json.JSONException
import org.json.JSONObject
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity(), PaymentResultListener {
    private lateinit var nameInput : TextInputEditText
    private lateinit var emailInput : TextInputEditText
    private lateinit var phoneInput : TextInputEditText
    private lateinit var amountInput : TextInputEditText
    private lateinit var submitBtn : Chip
    private  var amount :Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        nameInput = findViewById(R.id.name_inp)
        emailInput = findViewById(R.id.email_inp)
        phoneInput = findViewById(R.id.phone_inp)
        amountInput = findViewById(R.id.amount_inp)
        submitBtn = findViewById(R.id.submit_btn)

        submitBtn.setOnClickListener {
            amount = (amountInput.text.toString().toFloat()*100).roundToInt()
            val checkOut = Checkout()

            checkOut.setKeyID("rzp_test_UgBLp2JyQ22BQj")
            checkOut.setImage(R.drawable.ic_launcher_background)
            val obj= JSONObject()
            try {
                obj.put("name", nameInput.text.toString())
                obj.put("description","Razor Pay Payment")
                obj.put("theme.color", "")
                obj.put("currency","INR")
                obj.put("amount", amount)
                obj.put("prefill.contact", phoneInput.text.toString())
                obj.put("prefill.email", emailInput.text.toString())

                nameInput.text?.clear()
                emailInput.text?.clear()
                phoneInput.text?.clear()
                amountInput.text?.clear()
                checkOut.open(this@MainActivity,obj)

            }
            catch (e: JSONException) {
                e.stackTrace
            }
        }

    }
    override fun onPaymentSuccess(p0: String?) {
        Snackbar.make(nameInput,p0.toString(), Snackbar.LENGTH_SHORT).show()
    }

    override fun onPaymentError(p0: Int, p1: String?) {
        Snackbar.make(nameInput,p1.toString(), Snackbar.LENGTH_SHORT).show()

    }
}