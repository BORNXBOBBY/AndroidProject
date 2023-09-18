package com.example.yati.activities

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import androidx.appcompat.app.AlertDialog
import com.example.yati.R


open class YatiActivity:Activity() {
    private lateinit var dialog: AlertDialog

    fun displayLoaderActivity() {
        val builder= this.let { AlertDialog.Builder(it) }
        val dialogView= layoutInflater.inflate(R.layout.progressbar_layout,null)
        builder.setView(dialogView)
        builder.setCancelable(false)
        dialog = builder.create()
        dialog.show()
    }

    fun hieLoaderActivity() {
        dialog.dismiss()
    }

    fun isOnline():Boolean{
        val conManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val internetInfo =conManager.activeNetworkInfo
        return internetInfo!=null && internetInfo.isConnected
    }
}