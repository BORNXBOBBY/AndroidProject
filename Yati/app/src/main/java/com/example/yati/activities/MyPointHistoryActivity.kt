package com.example.yati.activities

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.yati.R
import com.example.yati.global.AppGlobal
import com.example.yati.models.PointRecord
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_my_point_history.*
import com.example.yati.adapters.PointAdapter as PointAdapter


class MyPointHistoryActivity :YatiActivity() {
    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var myPointHistory : List<PointRecord> = ArrayList()
    private var myPointHistoryAdapter : PointAdapter = PointAdapter(myPointHistory)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_point_history)

        if (isOnline()){
            displayLoaderActivity()
            loadMyPointsHistory()
        }else{
            showAlertDialog()
        }
    }

    fun loadMyPointsHistory(){

        firestore.collection("Points").whereEqualTo("user_id", AppGlobal.userid)
            .get()
            .addOnSuccessListener {
                myPointHistory = it.toObjects(PointRecord::class.java)
                myPointHistoryAdapter.dataSource = myPointHistory
                myPointHistoryAdapter.notifyDataSetChanged()
               renderPointsHistory(myPointHistory)
            }
            .addOnFailureListener {
                Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show()
            }
    }

    private fun renderPointsHistory(myPointHistory: List<PointRecord>) {
        hieLoaderActivity()
        if (myPointHistory.size == 0){
            myPoint_transaction_has_not_information_text.visibility = View.VISIBLE
        }
        else {
            order_recyclerview.layoutManager = LinearLayoutManager(this)
            order_recyclerview.adapter = PointAdapter(myPointHistory)
        }
    }

    fun showAlertDialog(){
        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(this)
        alertDialog.setTitle("Connection Problem")
        alertDialog.setMessage("Please check your Connection!")
        alertDialog.setIcon(R.drawable.ic_baseline_warning)
        alertDialog.setPositiveButton("Try Again"){ dialog, id ->

            if (isOnline()){
                displayLoaderActivity()
                loadMyPointsHistory()
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