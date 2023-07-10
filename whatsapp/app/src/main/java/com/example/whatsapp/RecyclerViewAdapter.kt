package com.example.whatsapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide

class RecyclerViewAdapter(val context: Context,val dataList :List<DataModel>,val setOnClick:setOnClick):
 RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>(){
     override fun onCreateViewHolder(parent: ViewGroup, viewType:Int): RecyclerViewAdapter.ViewHolder {
         val view = LayoutInflater.from(parent.context)
             .inflate(R.layout.customerlistview, parent,  false)
         return ViewHolder(view)
     }

    override fun onBindViewHolder(holder: RecyclerViewAdapter.ViewHolder, position: Int) {
        val data=dataList[position]
        holder.description.text=data.description

        holder.title.text=data.title
        Glide.with(context).load(data.image).into(holder.image)
        holder.itemView.setOnClickListener {
            setOnClick.onClick(position)
        }
    }

    override fun getItemCount(): Int {
      return dataList.size
    }
    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val image=itemView.findViewById<ImageView>(R.id.image)
        val title=itemView.findViewById<TextView>(R.id.title)
        val description=itemView.findViewById<TextView>(R.id.description)
    }
}
interface setOnClick {
    fun onClick(position: Int)
}