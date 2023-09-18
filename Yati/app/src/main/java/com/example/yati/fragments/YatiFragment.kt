package com.example.yati.fragments

import android.content.Context
import android.net.ConnectivityManager
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AlertDialog
import com.example.yati.R

open class YatiFragment : Fragment() {

    private lateinit var dialog:AlertDialog

    fun displayLoader() {
        val builder= activity?.let { AlertDialog.Builder(it) }
        val dialogView= layoutInflater.inflate(R.layout.progressbar_layout,null)
        builder?.setView(dialogView)
        builder?.setCancelable(false)
        dialog = builder?.create()!!
        dialog.show()
    }

    fun hieLoader() {
        dialog.dismiss()
    }

    fun isOnline():Boolean{
        val conManager = activity?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val internetInfo =conManager.activeNetworkInfo
        return internetInfo != null && internetInfo.isConnectedOrConnecting
    }
}