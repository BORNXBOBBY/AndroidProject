package com.example.classtest24august

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class Adapter(val context: Context,val dataList: List<DataModel>): BaseAdapter() {
    override fun getCount(): Int {
       return dataList.size
    }

    override fun getItem(p0: Int): Any {
      return dataList[p0]
    }

    override fun getItemId(p0: Int): Long {
       return p0.toLong()
    }

    @SuppressLint("MissingInflatedId")
    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
       val rowView=LayoutInflater.from(context).inflate(R.layout.list,p2,false)
        val name=rowView.findViewById<TextView>(R.id.name)
        name.text=dataList[p0].name
        return rowView
    }
}