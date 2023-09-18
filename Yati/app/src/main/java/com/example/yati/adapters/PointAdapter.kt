package com.example.yati.adapters

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.yati.R
import com.example.yati.models.PointRecord
import kotlinx.android.synthetic.main.point_list_itam.view.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class PointAdapter (var dataSource: List<PointRecord>): RecyclerView.Adapter<PointAdapter.PointViewHolder>(){

    val months = arrayOf("","Jan","Feb","Mar", "Apr", "May","Jun","Jul", "Aug", "Sep","Oct", "Nov", "Dec")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PointViewHolder {
        return PointViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.point_list_itam,parent,false)
        )
    }

    override fun getItemCount() = dataSource.size
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: PointViewHolder, position: Int) {

        val data= dataSource[position]

        if (data.date != ""){

            var simpleFormatter = DateTimeFormatter.ISO_DATE_TIME
            var convertedDate = LocalDateTime.parse(data.date,simpleFormatter)

            holder.view.point_day_of_tran.text=convertedDate.dayOfMonth.toString()

            holder.view.point_month_of_tran.text = months[convertedDate.monthValue]
        }
        if (data.transaction_type == "Refferal"){
            holder.view.point_type_image.setImageResource(R.drawable.type_referral)
        }

        if(data.transaction_type == "Transaction"){
            holder.view.point_type_image.setImageResource(R.drawable.type_others)
        }
        
//        holder.view.point_type_image.setImageDrawable(R.id.user_image)

        holder.view.point_earn.text = data.points.toString() + " Points"
        holder.view.point_transdaction_type.text = data.transaction_type
        holder.view.point_transection_number.text = data.transaction_number.toString()
    }

    class PointViewHolder(val view: View): RecyclerView.ViewHolder(view)
}