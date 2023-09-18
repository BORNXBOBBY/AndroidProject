package com.example.yati.adapters

import android.os.Build
import android.text.Html
import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.yati.R
import com.example.yati.models.ServiceModels
import kotlinx.android.synthetic.main.service_list_itam.view.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ServiceAdapter (var modelsSource:List<ServiceModels>, private val onRateReviewClickListener: OnRateReviewClickListener):RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder>(){

    val months = arrayOf("","Jan","Feb","Mar", "Apr", "May","Jun","Jul", "Aug", "Sep","Oct", "Nov", "Dec")
    val images= arrayOf(R.drawable.type_bonus, R.drawable.type_fail)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {
        return ServiceViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.service_list_itam,parent,false),onRateReviewClickListener
        )
    }

    override fun getItemCount()= modelsSource.size
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {

        val data= modelsSource[position]
        holder.serviceModels = data

        var format = DateTimeFormatter.ISO_DATE_TIME

            if (data.requested_date_time != "") {
                val convertedRequestedDateTime = LocalDateTime.parse(data.requested_date_time, format)
                holder.view.myService_day_of_tran.text = convertedRequestedDateTime.dayOfMonth.toString()
                holder.view.myService_month_of_tran.text = months[convertedRequestedDateTime.monthValue]
            }

        holder.view.myService_title.text = data.bike_name
//        holder.view.myService_body.text = data.service_body

        var serviceNameFromHTML : Spanned

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            serviceNameFromHTML = Html.fromHtml(data.problem_in_bike.toString(),Html.FROM_HTML_MODE_COMPACT)
        }else{
            serviceNameFromHTML = Html.fromHtml(data.problem_in_bike.toString())
        }
        holder.view.myService_body.text = serviceNameFromHTML

        holder.view.myService_currentStatus.text = data.status

       if (data.status == "Delivered"){

            holder.view.rating_Bar.visibility = View.VISIBLE
            holder.view.rating_text.visibility = View.VISIBLE
            holder.view.rate_review_button.visibility = View.GONE
           if (data.delivered_date_time != "") {
               val convertedDeliveredDateTime = LocalDateTime.parse(data.delivered_date_time, format)
               holder.view.myService_day_of_tran.text = convertedDeliveredDateTime.dayOfMonth.toString()
               holder.view.myService_month_of_tran.text = months[convertedDeliveredDateTime.monthValue]
           }
           if(data.rating != 0) {
               holder.view.rating_Bar.rating = data.rating!!.toFloat()
               holder.view.rating_Bar.setIsIndicator(true)
           }
        }

        if(data.status == "Requested"){
                holder.view.myService_type_image.setImageResource(R.drawable.type_service)
        }
        if(data.status == "Arrived"){
            holder.view.myService_type_image.setImageResource(R.drawable.type_success)
        }
    }

    class ServiceViewHolder(val view: View, onRateReviewClickListener: OnRateReviewClickListener):RecyclerView.ViewHolder(view){
        lateinit var serviceModels : ServiceModels

        init {
            view.rate_review_button.setOnClickListener {
                onRateReviewClickListener.rateReviewClick(serviceModels, view)
            }
            view.rating_Bar.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
                onRateReviewClickListener.onRatingChange(serviceModels, view ,ratingBar, rating, fromUser)
            }
        }

    }
}

interface OnRateReviewClickListener{
    fun rateReviewClick(serviceModels: ServiceModels, view: View)
    fun onRatingChange(serviceModels: ServiceModels, view: View, ratingBar: RatingBar, rating: Float, fromUser: Boolean)
}




