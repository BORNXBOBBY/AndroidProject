package com.example.yati.adapters

import android.annotation.SuppressLint
import android.text.Html
import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.yati.R
import com.example.yati.global.AppGlobal
import com.example.yati.models.HomeRecords
import kotlinx.android.synthetic.main.homelist_item.view.*

class HomeAdapter (var dataSource:List<HomeRecords>, private val onUserClickListener: OnUserClickListener): RecyclerView.Adapter<HomeAdapter.HomeViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        return HomeViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.homelist_item,parent,false), onUserClickListener
        )
    }

    override fun getItemCount() = dataSource.size
    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {

        val data= dataSource[position]
        holder.view.invite_text.text = data.title

        if (!data.description.isNullOrBlank()) {
            var descText: Spanned = Html.fromHtml(data.description.toString(), Html.FROM_HTML_MODE_COMPACT)
            holder.view.spread_text.text = descText
        } else {
            holder.view.spread_text.text = ""
        }

        holder.view.image_view.setImageResource(AppGlobal.getImageByType(data.activity_type.toString()))

        if (data.points != "false") {
            holder.view.point_text.text = data.points
        } else if (data.discount != "false") {
            holder.view.point_text.text = data.discount
        } else {
            if (data.activity_type == "Coupon") {
                holder.view.point_text.text = "Free"
            } else {
                holder.view.point_text.text = ""
            }
        }

        holder.userActivity = data

        if (data.activity_type == "Product"){
            holder.view.point_text.text = ""
            holder.view.spread_text.text = ""
            Glide.with(holder.view.context)
                .load(data.description).error(R.drawable.type_others)
                .into(holder.view.image_view)
        }
    }

    class HomeViewHolder(val view: View, onUserActivityClickListener: OnUserClickListener): RecyclerView.ViewHolder(view) {
        lateinit var userActivity: HomeRecords

        init {
            view.setOnClickListener {
            onUserActivityClickListener.onClick(userActivity)
            }
        }
    }
}
interface OnUserClickListener{
    fun onClick(userActivity: HomeRecords)
}