package com.example.yati.adapters

import android.graphics.Color
import android.text.Html
import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.yati.R
import com.example.yati.models.MyBikesData
import kotlinx.android.synthetic.main.bike_list_item.view.*

class MyBikesAdapter(var dataSource:List<MyBikesData>, private  val onListClickListener: OnListClickListener): RecyclerView.Adapter<MyBikesAdapter.MyBikeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyBikeViewHolder {
        val view =LayoutInflater.from(parent.context)
            .inflate(R.layout.bike_list_item, parent, false)
        return MyBikeViewHolder(view,onListClickListener)

    }

    override fun onBindViewHolder(holder:MyBikeViewHolder, position: Int) {
        val data = dataSource[position]

            holder.itemBike = data
            holder.view.registration_number.text = data.registration_number
            holder.view.date_textView.text = data.purchase_date

            var bikeNameFromHTML: Spanned =
                Html.fromHtml(data.bike_name.toString(), Html.FROM_HTML_MODE_COMPACT)
            holder.view.bike_name.text = bikeNameFromHTML
            holder.view.bike_name.setTextColor(Color.BLACK)

            Glide.with(holder.view.context)
                .load(data.bike_image)
                .into(holder.view.bike_image)
    }

    override fun getItemCount()= dataSource.size

    class MyBikeViewHolder(val view: View, onListClickListener: OnListClickListener): RecyclerView.ViewHolder(view) {
        lateinit var itemBike:MyBikesData

        init {
            view.BookAppointment_layout.setOnClickListener {
                onListClickListener.OnBookServiceClick(itemBike)
            }
        }

    }

}
 interface OnListClickListener{
     fun OnBookServiceClick(itemBike: MyBikesData)
 }
