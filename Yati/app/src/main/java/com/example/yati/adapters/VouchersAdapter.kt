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
import com.example.yati.models.VouchersModels
import kotlinx.android.synthetic.main.vouchers_item_list.view.*
import java.time.LocalDateTime
import java.util.*


class VouchersAdapter (var dataSource: List<VouchersModels>): RecyclerView.Adapter<VouchersAdapter.VouchersViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VouchersViewHolder {
            return VouchersViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.vouchers_item_list,parent,false))
        }

        @SuppressLint("ResourceAsColor")
        @RequiresApi(Build.VERSION_CODES.O)
        override fun onBindViewHolder(holder:VouchersViewHolder, position: Int) {
            val vouchersPosition = dataSource[position]

            var couponNameFromHTML: Spanned

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                couponNameFromHTML = Html.fromHtml(vouchersPosition.description.toString(), Html.FROM_HTML_MODE_COMPACT)
            }
            else {
                couponNameFromHTML = Html.fromHtml(vouchersPosition.description.toString())
            }

            holder.view.vouchers_description.text = couponNameFromHTML

            val calender = Calendar.getInstance()

            val year = calender.get(Calendar.YEAR)
            val month = calender.get(Calendar.MONTH) + 1
            val day = calender.get(Calendar.DAY_OF_MONTH)

            try {
                val convertedDate = LocalDateTime.parse(vouchersPosition.expiry_date)
                val exDate = convertedDate.toLocalDate()
                if (exDate.year > year) {
                    holder.view.vouchers_currentStatus.text = "Active"
                    holder.view.vouchers_currentStatus.setTextColor(Color.GREEN)
                }
                else if (exDate.year == year){
                    if (exDate.monthValue > month) {
                        holder.view.vouchers_currentStatus.text = "Active"
                        holder.view.vouchers_currentStatus.setTextColor(Color.GREEN)
                    }
                    else if (exDate.monthValue == month){
                         if (exDate.dayOfMonth >= day) {
                            holder.view.vouchers_currentStatus.text = "Active"
                            holder.view.vouchers_currentStatus.setTextColor(Color.GREEN)
                         }
                         else{
                            holder.view.vouchers_currentStatus.text ="Expire"
                            holder.view.vouchers_currentStatus.setTextColor(Color.RED)
                         }
                    }
                    else{
                        holder.view.vouchers_currentStatus.text ="Expire"
                        holder.view.vouchers_currentStatus.setTextColor(Color.RED)
                    }

                }else{
                    holder.view.vouchers_currentStatus.text ="Expire"
                    holder.view.vouchers_currentStatus.setTextColor(Color.RED)
                }
            }
            catch(e:Exception) {
                e.printStackTrace()
            }

            val status = vouchersPosition.status
            if (status == "Used"){
                holder.view.vouchers_currentStatus.text ="Used"
                holder.view.vouchers_currentStatus.setTextColor(Color.GREEN)
            }
        }

        override fun getItemCount(): Int {
            return dataSource.size
        }

        class VouchersViewHolder(val view: View) : RecyclerView.ViewHolder(view)
}