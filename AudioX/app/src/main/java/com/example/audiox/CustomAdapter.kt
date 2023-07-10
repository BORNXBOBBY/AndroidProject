package com.example.form_workshop

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.audiox.DataModel
import com.example.audiox.R


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
        var data=datalist[position]

        val title = rowView.findViewById<TextView>(R.id.tvName)
        val description = rowView.findViewById<TextView>(R.id.tvEmail)

        title.setText(data.name)
        description.setText(data.description)
        return rowView
    }
}