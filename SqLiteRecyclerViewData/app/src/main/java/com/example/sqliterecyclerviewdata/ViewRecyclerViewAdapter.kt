package com.example.sqliterecyclerviewdata

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView

class ViewRecyclerViewAdapter(val context: Context, val datalist:List<ViewDataModel>,val setonclick:ViewDataActivity) : RecyclerView.Adapter<ViewRecyclerViewAdapter.ViewHolder>() {
    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val listId=itemView.findViewById<TextView>(R.id.listId)
        val listContact=itemView.findViewById<TextView>(R.id.listContact)
        val listname=itemView.findViewById<TextView>(R.id.listName)
        val listEmail=itemView.findViewById<TextView>(R.id.listEmail)
        val listUserId=itemView.findViewById<TextView>(R.id.listUserId)
        val listPassword=itemView.findViewById<TextView>(R.id.listPassword)
        val upDateBtn=itemView.findViewById<Button>(R.id.updateDataBtn)

        val deleteData=itemView.findViewById<Button>(R.id.deleteData)


    }

    @SuppressLint("SuspiciousIndentation")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =LayoutInflater.from(parent.context).inflate(R.layout.activity_view_recycler_view_adapter,parent,false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return datalist.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data =datalist[position]
        holder.listname.text=data.name
        holder.listEmail.text=data.email
        holder.listUserId.text=data.userId
        holder.listPassword.text=data.password
        holder.listContact.text=data.contact
        holder.listId.text=data.id
        holder.upDateBtn.setOnClickListener {
            val intent=Intent(context,UpDateActivity::class.java)
            intent.putExtra("id", data.id)
            intent.putExtra("name",data.name)
            intent.putExtra("email",data.email)
            intent.putExtra("userId",data.userId)
            intent.putExtra("password",data.password)
            intent.putExtra("contact",data.contact)
            context.startActivities(arrayOf(intent))
        }
        holder.deleteData.setOnClickListener {
            val intent= Intent(context,UpDateActivity::class.java)
            intent.putExtra("id", data.id)
            intent.putExtra("name",data.name)
            intent.putExtra("email",data.email)
            intent.putExtra("userId",data.userId)
            intent.putExtra("password",data.password)
            intent.putExtra("contact",data.contact)
            context.startActivities(arrayOf(intent))
        }

    }

//    interface setOnClick{
//        fun onClick(position: Int)
//    }

}