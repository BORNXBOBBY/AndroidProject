package com.example.sqliterecyclerviewdata

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ViewDataActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId", "Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_data)

//
//        val layoutInflater=layoutInflater
//         val listView = layoutInflater.inflate(R.layout.viewdata_recycler_view,findViewById(R.id.listResult))
//
//        val listId=listView.findViewById<TextView>(R.id.listId)
//        val listContact=listView.findViewById<TextView>(R.id.listContact)
//        val listname=listView.findViewById<TextView>(R.id.listName)
//        val listEmail=listView.findViewById<TextView>(R.id.listEmail)
//        val listUserId=listView.findViewById<TextView>(R.id.listUserId)
//        val listPassword=listView.findViewById<TextView>(R.id.listPassword)
//
//        val db = DataBaseHelper(this, null)
//
//
//        val cursor = db.viewData()
//        cursor!!.moveToFirst()
//
//        cursor!!.moveToFirst()
//        listId.append(cursor.getString(cursor.getColumnIndex(DataBaseHelper.ID_COL))+"\n")
//        listname.append(cursor.getString(cursor.getColumnIndex(DataBaseHelper.NAME_COl)) + "\n")
//        listEmail.append(cursor.getString(cursor.getColumnIndex(DataBaseHelper.EMAIL_COL)) + "\n")
//        listUserId.append(cursor.getString(cursor.getColumnIndex(DataBaseHelper.USER_COL)) + "\n")
//        listPassword.append(cursor.getString(cursor.getColumnIndex(DataBaseHelper.PASS_COL)) + "\n")
//        listContact.append(cursor.getString(cursor.getColumnIndex(DataBaseHelper.CONTACT_COL)) + "\n")
//
//        while(cursor.moveToNext()){
//            listId.append(cursor.getString(cursor.getColumnIndex(DataBaseHelper.ID_COL))+"\n")
//            listname.append(cursor.getString(cursor.getColumnIndex(DataBaseHelper.NAME_COl)) + "\n")
//            listEmail.append(cursor.getString(cursor.getColumnIndex(DataBaseHelper.EMAIL_COL)) + "\n")
//            listUserId.append(cursor.getString(cursor.getColumnIndex(DataBaseHelper.USER_COL)) + "\n")
//            listPassword.append(cursor.getString(cursor.getColumnIndex(DataBaseHelper.PASS_COL)) + "\n")
//            listContact.append(cursor.getString(cursor.getColumnIndex(DataBaseHelper.CONTACT_COL)) + "\n")
//
//        }
//
//        // at last we close our cursor
//        cursor.close()


//        val data= arrayListOf<ViewDataModel>(
//            ViewDataModel("listname","dsfkjsjldkf","jdkas","askjkdsa","sd","jdks"),
//            ViewDataModel("dsa","ddsj","dsd","sdsa","sd","sd")
//        )


        val db = DataBaseHelper(this, null)
        var listofData: ArrayList<ViewDataModel> = db.listOfUserInfo()
        val recyclerView=findViewById<RecyclerView>(R.id.recyclerview)
        recyclerView.layoutManager= GridLayoutManager(this,1)
        val recyclerAdapter=ViewRecyclerViewAdapter(this,listofData,this)
        recyclerView.adapter=recyclerAdapter
    }

//    fun onClick(position: Int) {
//        when(position){
//            position->startActivity(Intent(this,UpDateActivity::class.java))
//        }
//    }
}