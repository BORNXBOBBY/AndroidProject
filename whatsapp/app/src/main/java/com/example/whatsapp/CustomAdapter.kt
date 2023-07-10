package com.example.form_workshop

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.whatsapp.DataModel
import com.example.whatsapp.R

class CustomAdpater(val context: Context,val datalist:ArrayList<DataModel>):BaseAdapter() {
    override fun getCount(): Int {
        return datalist.size
    }

    override fun getItem(position: Int): Any {
        return datalist[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val rowView = LayoutInflater.from(context).inflate(R.layout.customerlistview, parent, false)
        val img = rowView.findViewById<ImageView>(R.id.image)
        var data=datalist[position]

       Glide.with(context).load(data.image).error(R.drawable.baseline_person_24).into(img)
        val title = rowView.findViewById<TextView>(R.id.title)
        val description = rowView.findViewById<TextView>(R.id.description)

        title.setText(data.title)
        description.setText(data.description)
        return rowView
    }
}