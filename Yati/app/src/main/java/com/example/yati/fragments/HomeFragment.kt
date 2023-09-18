package com.example.yati.fragments

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.yati.R
import com.example.yati.activities.CouponActivity
import com.example.yati.activities.MyPointHistoryActivity
import com.example.yati.activities.MyServicesActivity
import com.example.yati.adapters.HomeAdapter
import com.example.yati.adapters.OnUserClickListener
import com.example.yati.global.AppGlobal
import com.example.yati.models.HomeRecords
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.home_fragment.*

class HomeFragment : YatiFragment(),OnUserClickListener {
    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var userHomeActivity : List<HomeRecords> = ArrayList()
    private var userHomeAdapter : HomeAdapter = HomeAdapter(userHomeActivity, this)
    companion object {
        fun newInstance() = HomeFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.home_fragment, container, false)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (isOnline()){
            displayLoader()
            loadUserActivities()
        }else{
            showAlertDialog()
        }
    }

    private fun loadUserActivities(){

        firestore.collection("Users Activities").whereEqualTo("user_id", AppGlobal.userid)
            .get()
            .addOnSuccessListener {
                userHomeActivity = it.toObjects(HomeRecords::class.java)
                userHomeAdapter.dataSource = userHomeActivity
                userHomeAdapter.notifyDataSetChanged()
                renderUserActivities(userHomeActivity)
            }
            .addOnFailureListener {
                hieLoader()
            } 
    }

    private fun renderUserActivities(userActivity: List<HomeRecords>){
        hieLoader()
        val layoutManager= LinearLayoutManager(this.context,
        LinearLayoutManager.HORIZONTAL, false)
        homerecyclerView.setLayoutManager(layoutManager)

        val completeList = userActivity.plus(AppGlobal.genericUserActivityList)
        homerecyclerView.adapter= HomeAdapter(completeList,this)
    }

    private fun showAlertDialog(){
        val alertDialog:AlertDialog.Builder = AlertDialog.Builder(activity)
        alertDialog.setTitle("Connection Problem")
        alertDialog.setMessage("Please check your Connection!")
        alertDialog.setIcon(R.drawable.ic_baseline_warning)
        alertDialog.setPositiveButton("Try Again"){ dialog, id ->
          if (isOnline()){
             Toast.makeText(activity,"Your are Online!",Toast.LENGTH_SHORT).show()
              displayLoader()
              loadUserActivities()
          }else{
           showAlertDialog()
              Toast.makeText(activity,"Please Go Online!",Toast.LENGTH_SHORT).show()
          }
        }
        alertDialog.setNegativeButton("Cancel"){dialog, id ->
            dialog.dismiss()
        }

        val alert = alertDialog.create()
        alert.setCanceledOnTouchOutside(false)
        alert.show()
        val negativeButton = alert.getButton(DialogInterface.BUTTON_NEGATIVE)
        negativeButton.setTextColor(Color.RED)

        val positiveButton = alert.getButton(DialogInterface.BUTTON_POSITIVE)
        positiveButton.setTextColor(Color.GREEN)
    }

    override fun onClick(userActivity: HomeRecords) {
        val type = userActivity.activity_type

        if (type == "Service"){
            val intent = Intent(activity, MyServicesActivity::class.java)
            startActivity(intent)
        }
        else if (type == "Product"){
            val navController = this.activity?.findNavController(R.id.navigation_host)
            navController?.navigate(R.id.product_nav)
        }
        else if (type == "Coupon"){
            val intent = Intent(activity,CouponActivity::class.java)
            startActivity(intent)
        }
        else{
            val intent = Intent(activity, MyPointHistoryActivity::class.java)
            startActivity(intent)
        }
    }
}