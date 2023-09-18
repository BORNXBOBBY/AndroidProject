package com.example.yati.adapters

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.yati.R
import com.example.yati.models.CouponsModels
import kotlinx.android.synthetic.main.coupons_list_item.view.*
import kotlinx.android.synthetic.main.vouchers_item_list.view.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

class CouponsAdapter(var dataSource: List<CouponsModels>):RecyclerView.Adapter<CouponsAdapter.CouponsViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CouponsViewHolder {
        return CouponsViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.coupons_list_item,parent,false))
    }

    @SuppressLint("ResourceAsColor")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder:CouponsViewHolder, position: Int) {
        val couponPosition = dataSource[position]

        var couponNameFromHTML: Spanned

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            couponNameFromHTML = Html.fromHtml(couponPosition.description.toString(), Html.FROM_HTML_MODE_COMPACT)
        }
        else {
            couponNameFromHTML = Html.fromHtml(couponPosition.description.toString())
        }

        holder.view.coupons_description.text = couponNameFromHTML

        val calender = Calendar.getInstance()

        val year = calender.get(Calendar.YEAR)
        val month = calender.get(Calendar.MONTH) + 1
        val day = calender.get(Calendar.DAY_OF_MONTH)

        try {
            val convertedDate = LocalDateTime.parse(couponPosition.expiry_date)
            val exDate = convertedDate.toLocalDate()
            if (exDate.year > year) {
                holder.view.coupons_currentStatus.text = "Active"
                holder.view.coupons_currentStatus.setTextColor(Color.GREEN)
            }
            else if (exDate.year == year){
                if (exDate.monthValue > month) {
                    holder.view.coupons_currentStatus.text = "Active"
                    holder.view.coupons_currentStatus.setTextColor(Color.GREEN)
                }
                else if (exDate.monthValue == month){
                    if (exDate.dayOfMonth >= day) {
                        holder.view.coupons_currentStatus.text = "Active"
                        holder.view.coupons_currentStatus.setTextColor(Color.GREEN)
                    }
                    else{
                        holder.view.coupons_currentStatus.text ="Expire"
                        holder.view.coupons_currentStatus.setTextColor(Color.RED)
                    }
                }
                else{
                    holder.view.coupons_currentStatus.text ="Expire"
                    holder.view.coupons_currentStatus.setTextColor(Color.RED)
                }

            }else{
                holder.view.coupons_currentStatus.text ="Expire"
                holder.view.coupons_currentStatus.setTextColor(Color.RED)
            }
        }
        catch(e:Exception) {
            e.printStackTrace()
        }

        val status = couponPosition.status
        if (status == "Used"){
            holder.view.coupons_currentStatus.text ="Used"
            holder.view.coupons_currentStatus.setTextColor(Color.GREEN)
        }
    }

    override fun getItemCount(): Int {
        return dataSource.size
    }

    class CouponsViewHolder(val view: View) : RecyclerView.ViewHolder(view)
}


