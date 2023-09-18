package com.example.yati.activities

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.Spanned
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.yati.R
import com.example.yati.adapters.MyBikesAdapter
import com.example.yati.adapters.OnListClickListener
import com.example.yati.global.AppGlobal
import com.example.yati.models.MyBikesData
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_my_bykes.*

class MyBikesActivity : YatiActivity() ,OnListClickListener{
    private lateinit var firestore: FirebaseFirestore
    private var myBikesData : List<MyBikesData> = ArrayList()
    private var myBikesAdapter:MyBikesAdapter = MyBikesAdapter(myBikesData, this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_bykes)
        firestore = FirebaseFirestore.getInstance()

        if (isOnline()){
            loadMyBikesApi()
            displayLoaderActivity()
        }else{
            showAlertDialog()
        }

    }

    private fun loadMyBikesApi(){
        firestore.collection("Users").document(AppGlobal.userid.toString()).collection("My Bikes")
            .get()
            .addOnCompleteListener {
                hieLoaderActivity()
                myBikesData = it.result!!.toObjects(MyBikesData::class.java)
                myBikesAdapter.dataSource = myBikesData
                myBikesAdapter.notifyDataSetChanged()
                renderHistory(myBikesData)
            }
            .addOnFailureListener {
                hieLoaderActivity()
                it.printStackTrace()
                showAlertDialog()
                Toast.makeText(this, "data retrieving failed", Toast.LENGTH_SHORT).show()
            }
    }

    private fun renderHistory(myBikesData: List<MyBikesData>) {

        if (myBikesData.size == 0){
            myBike_has_notInformation_text.visibility = View.VISIBLE
        }
        else {
                bike_recyclerview.layoutManager = LinearLayoutManager(this)
                bike_recyclerview.adapter = MyBikesAdapter(myBikesData, this)
        }
  }

    private fun showAlertDialog(){
        val alertDialog:AlertDialog.Builder = AlertDialog.Builder(this)
        alertDialog.setTitle("Connection Problem")
        alertDialog.setMessage("Please check your Connection!")
        alertDialog.setIcon(R.drawable.ic_baseline_warning)
        alertDialog.setPositiveButton("Try Again"){ dialog, id ->

            if (isOnline()){
                displayLoaderActivity()
                loadMyBikesApi()
                Toast.makeText(this,"You are Online!",Toast.LENGTH_SHORT).show()
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

    override fun OnBookServiceClick(itemBike: MyBikesData) {
        val intent = Intent(this,BookServiceActivity::class.java)

        var bikeNameFromHTML:Spanned
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            bikeNameFromHTML = Html.fromHtml(itemBike.bike_name.toString(), Html.FROM_HTML_MODE_LEGACY)
        } else {
            bikeNameFromHTML = Html.fromHtml(itemBike.bike_name.toString())
        }

        intent.putExtra("bikeId",itemBike.product_id.toString())
        intent.putExtra("bikeName", bikeNameFromHTML.toString())
        startActivity(intent)
    }
}