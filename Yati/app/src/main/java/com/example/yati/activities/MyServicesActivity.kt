package com.example.yati.activities

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.Spanned
import android.view.Gravity
import android.view.View
import android.widget.EditText
import android.widget.RatingBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.yati.R
import com.example.yati.adapters.OnRateReviewClickListener
import com.example.yati.adapters.ServiceAdapter
import com.example.yati.global.AppGlobal
import com.example.yati.models.*
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_my_services.*
import kotlinx.android.synthetic.main.send_feedback_layout.view.*
import kotlinx.android.synthetic.main.service_list_itam.view.*

class MyServicesActivity : YatiActivity(), OnRateReviewClickListener {
    private var firestore:FirebaseFirestore = FirebaseFirestore.getInstance()
    private var myServiceList : List<ServiceModels> = ArrayList()
    private var myServiceAdapter : ServiceAdapter = ServiceAdapter(myServiceList,this)
    private  var ratings = 0
    private lateinit var feedBackText :EditText

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_services)

        if (isOnline()){
            displayLoaderActivity()
            loadMyServiceDetails()

        }else{
            showAlertDialog()
        }
    }

    private fun loadMyServiceDetails() {

        firestore.collection("Services").orderBy("user_id")
            .startAt(AppGlobal.userid.toString())
            .get()
            .addOnSuccessListener {
                myServiceList = it.toObjects(ServiceModels::class.java)
                myServiceAdapter.modelsSource = myServiceList
                myServiceAdapter.notifyDataSetChanged()
               renderHistory(myServiceList)
            }
            .addOnFailureListener {
                Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show()
            }
        }

    private fun renderHistory(serviceList:List<ServiceModels>) {
        hieLoaderActivity()
        if (serviceList.size == 0){
            myService_has_not_information_text.visibility = View.VISIBLE
        }
        else {
            service_recyclerview.layoutManager = LinearLayoutManager(this)
            service_recyclerview.adapter = ServiceAdapter(serviceList,this)
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
                loadMyServiceDetails()
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

    override fun rateReviewClick(serviceModels: ServiceModels, view: View) {
         ratings = view.rating_Bar.rating.toInt()
        val inflater = layoutInflater
        val inflateView = inflater.inflate(R.layout.send_feedback_layout, null)
        val feedbackRating = inflateView.findViewById<RatingBar>(R.id.feedback_ratingBar)
        feedbackRating.rating = ratings.toFloat()
        inflateView.feedback_ratingBar.setIsIndicator(true)
        feedBackText = inflateView.findViewById(R.id.sendFeedback_editText)

        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(this, R.style.CustomAlertDialog)
        alertDialog.setTitle("Rate Service & Give Us Your Feedback")

        if (serviceModels.feedback.toString() == "") {

            alertDialog.setPositiveButton("Send Feedback") { dialog, id ->

                displayLoaderActivity()
                loadFeedBack(serviceModels, view)

            }
            alertDialog.setNegativeButton("May Be Later") { dialog, id ->
                dialog.dismiss()
            }
            val feedbackAlert = alertDialog.create()
            feedbackAlert.setCanceledOnTouchOutside(false)
            feedbackAlert.window!!.setGravity(Gravity.BOTTOM)
            feedbackAlert.setView(inflateView)
            feedbackAlert.show()
            val negativeButton = feedbackAlert.getButton(DialogInterface.BUTTON_NEGATIVE)
            negativeButton.setTextColor(Color.RED)

            val positiveButton = feedbackAlert.getButton(DialogInterface.BUTTON_POSITIVE)
            positiveButton.setTextColor(Color.GREEN)

        }
        else{
            val cardView = inflateView.findViewById<CardView>(R.id.card)
            cardView.visibility = View.VISIBLE
            feedBackText.setText(serviceModels.feedback.toString())
            inflateView.sendFeedback_editText.setEnabled(false)
            val alert = alertDialog.create()
            alert.setCanceledOnTouchOutside(true)
            alert.window!!.setGravity(Gravity.BOTTOM)
            alert.setView(inflateView)
            alert.show()
        }
    }

    private fun loadRating(serviceData:ServiceModels, serviceRating: String, view: View){
        val updateRatingRequest:MutableMap<String , Any> = HashMap()
        updateRatingRequest["rating"] = ratings
        firestore.collection("Services").document(serviceData.id.toString())
            .update(updateRatingRequest)
            .addOnCompleteListener {
                hieLoaderActivity()
                view.rating_Bar.setIsIndicator(true)
                Toast.makeText(this, "Thanks For Rating", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                hieLoaderActivity()
                Toast.makeText(this, " Rating Failed ", Toast.LENGTH_SHORT).show()
            }
    }
    private fun loadFeedBack(serviceData: ServiceModels, view: View){

        val feedbackTextValue= feedBackText.text.toString().trim()
        val updateFeedbackRequest:MutableMap<String , Any> = HashMap()

        updateFeedbackRequest["feedback"] = feedbackTextValue
        firestore.collection("Services").document(serviceData.id.toString())
            .update(updateFeedbackRequest)
            .addOnCompleteListener {
                hieLoaderActivity()
                view.rating_Bar.setIsIndicator(true)
                Toast.makeText(this, "Thanks For Giving Us Your Feedback", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                hieLoaderActivity()
                Toast.makeText(this, "Please try again", Toast.LENGTH_SHORT).show()
            }
    }
    
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onRatingChange(serviceModels: ServiceModels, view: View, ratingBar: RatingBar, rating: Float, fromUser: Boolean) {
        ratings = rating.toInt()
        when(rating.toInt()){
            0.0.toInt()->{
                view.rating_Bar.visibility = View.VISIBLE
                view.rating_text.visibility = View.VISIBLE
                view.rate_review_button.visibility = View.GONE

            }
            else ->{
                view.rating_Bar.visibility = View.VISIBLE
                view.rating_text.visibility = View.GONE
                view.rate_review_button.visibility = View.VISIBLE

                if (serviceModels.rating?.toInt() != 0 && serviceModels.feedback !=""){
                    view.rate_review_button.setText("Rewrite")
                }
                if (serviceModels.rating?.toInt() == 0){
                    displayLoaderActivity()
                    val rate = ratings.toString()
                    loadRating(serviceModels, rate,view)
                }
            }
        }
    }
}
