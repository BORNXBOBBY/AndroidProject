package com.example.testall

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class firestoreAdapter(val context: Context, var arrayList: List<DataModel>): BaseAdapter() {
    override fun getCount(): Int {
        return  arrayList.size
    }

    override fun getItem(p0: Int): Any {
        return  arrayList[p0]
    }

    override fun getItemId(p0: Int): Long {
        return  p0.toLong()
    }

    @SuppressLint("MissingInflatedId", "ViewHolder")
    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val rowView= LayoutInflater.from(context).inflate(R.layout.firestore_data,p2,false)
        val data=arrayList[p0]
        rowView.findViewById<TextView>(R.id.username).text=data.username
        rowView.findViewById<TextView>(R.id.password).text=data.password
        return  rowView
    }
}