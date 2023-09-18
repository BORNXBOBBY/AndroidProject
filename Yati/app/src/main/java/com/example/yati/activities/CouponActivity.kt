package com.example.yati.activities

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.yati.R
import com.example.yati.adapters.CouponsAdapter
import com.example.yati.global.AppGlobal
import com.example.yati.models.CouponsModels
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_coupons.*

class CouponActivity : YatiActivity() {
    private var firestore : FirebaseFirestore = FirebaseFirestore.getInstance()
    private var couponsList : List<CouponsModels>  = ArrayList()
    private var couponsAdapter : CouponsAdapter = CouponsAdapter(couponsList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coupons)

        if (isOnline()){
            displayLoaderActivity()
            loadCouponData()

        }else{
            showAlertDialog()
        }
    }

    private fun loadCouponData() {
        firestore.collection("Coupons").whereEqualTo("user_id", AppGlobal.userid)
            .get()
            .addOnSuccessListener {
                hieLoaderActivity()
                couponsList = it.toObjects(CouponsModels::class.java)
                couponsAdapter.dataSource = couponsList

                couponsAdapter.notifyDataSetChanged()
                renderCoupons(couponsList)
            }
            .addOnFailureListener {
                hieLoaderActivity()
                Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show()
            }
    }

    private fun renderCoupons(coupons: List<CouponsModels>) {
        hieLoaderActivity()
        if (coupons.size == 0){
            coupon_text.visibility = View.VISIBLE
        }else {
            coupons_recyclerview.layoutManager = LinearLayoutManager(this)
            coupons_recyclerview.adapter = CouponsAdapter(coupons)
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
                loadCouponData()
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