package com.example.yati.activities

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.yati.R
import com.example.yati.adapters.VouchersAdapter
import com.example.yati.global.AppGlobal
import com.example.yati.models.VouchersModels
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_vouchers.*


class VouchersActivity : YatiActivity() {
    private var firestore : FirebaseFirestore = FirebaseFirestore.getInstance()
    private var vouchersList : List<VouchersModels>  = ArrayList()
    private var vouchersAdapter : VouchersAdapter = VouchersAdapter(vouchersList)
    private lateinit var vouchersTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vouchers)
        vouchersTextView = findViewById(R.id.vouchers_text)
        if (isOnline()){
            displayLoaderActivity()
            loadVouchers()

        }else{
            showAlertDialog()
        }
    }

    private fun loadVouchers() {
        firestore.collection("Vouchers")
            .whereEqualTo("user_id", AppGlobal.userid)
            .get()
            .addOnSuccessListener {
                vouchersList = it.toObjects(VouchersModels::class.java)
                vouchersAdapter.dataSource = vouchersList
                vouchersAdapter.notifyDataSetChanged()
                hieLoaderActivity()
                renderCoupons(vouchersList)
            }
            .addOnFailureListener {
                hieLoaderActivity()
                Toast.makeText(this, "Failed to loading data", Toast.LENGTH_SHORT).show()
            }
    }

    private fun renderCoupons(vouchers: List<VouchersModels>) {
        hieLoaderActivity()
        if (vouchers.size == 0){
            vouchersTextView.visibility = View.VISIBLE
        }else {
            vouchers_recyclerview.layoutManager = LinearLayoutManager(this)
            vouchers_recyclerview.adapter = VouchersAdapter(vouchers)
        }
    }

    private fun showAlertDialog(){
        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(this)
        alertDialog.setTitle("Connection Problem")
        alertDialog.setMessage("Please check your Connection!")
        alertDialog.setIcon(R.drawable.ic_baseline_warning)
        alertDialog.setPositiveButton("Try Again"){ dialog, id ->

            if (isOnline()){
                displayLoaderActivity()
                loadVouchers()
                Toast.makeText(this,"You are Online!", Toast.LENGTH_SHORT).show()
            }else{
                showAlertDialog()
                Toast.makeText(this,"Please Go Online!", Toast.LENGTH_SHORT).show()
            }
        }
        alertDialog.setNegativeButton("Cancel"){dialog, id ->
            dialog.dismiss()
        }

        val alert = alertDialog.create()
        alert.setCanceledOnTouchOutside(false)
        alert.show()
        val negativeButton = alert.getButton(DialogInterface.BUTTON_NEGATIVE)
        negativeButton.setTextColor(Color.RED)

        val positiveButton = alert.getButton(DialogInterface.BUTTON_POSITIVE)
        positiveButton.setTextColor(Color.GREEN)
    }
}