package com.example.yati.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.yati.R
import com.example.yati.activities.*
import com.example.yati.global.AppGlobal
import com.google.firebase.auth.FirebaseAuth


class MyProfileFragment : Fragment() {
    lateinit var signOutActivity: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view=inflater.inflate(R.layout.myprofile_fragment, container, false)

        //val userNameId = view.findViewById<TextView>(R.id.user_nameText)
        signOutActivity = view.findViewById(R.id.signOut_text)
        signOutActivity.setOnClickListener {

            val savePreference = this.requireActivity().getSharedPreferences(
                "loginPref",
                Context.MODE_PRIVATE
            )
            val editor: SharedPreferences.Editor = savePreference.edit()
            editor.clear()
            editor.apply()

            val auth:FirebaseAuth = FirebaseAuth.getInstance()
            auth.signOut()

            AppGlobal.userFullName = ""
            AppGlobal.userid = ""
            AppGlobal.AppToken = ""
            AppGlobal.userShortName = ""
            AppGlobal.loyaltyPoint = ""
            AppGlobal.referralUserId = ""

            val intent = Intent(activity, SigninActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }

        //userNameId.setText(AppGlobal.userFullName)

        val profileActivity = view.findViewById<TextView>(R.id.profile_text)
        profileActivity.setOnClickListener {
            val intent = Intent(activity, ProfileActivity::class.java)
            startActivity(intent)
        }
         val vouchersActivity = view.findViewById<TextView>(R.id.vouchers_textView)
         vouchersActivity.setOnClickListener {
            val intent = Intent(activity, VouchersActivity::class.java)
            startActivity(intent)
        }
        val addressActivity = view.findViewById<TextView>(R.id.address_textView)
        addressActivity.setOnClickListener {
            val intent = Intent(activity, CouponActivity::class.java)
            startActivity(intent)
        }
        val myServicesActivity = view.findViewById<TextView>(R.id.myServices_text)
        myServicesActivity.setOnClickListener {
            val intent = Intent(activity, MyServicesActivity::class.java)
            startActivity(intent)
        }
        val bookServiceActivity = view.findViewById<TextView>(R.id.bookService_text)
        bookServiceActivity.setOnClickListener {
            val intent = Intent(activity, BookServiceActivity::class.java)
            startActivity(intent)
        }
        val myBikesActivity = view.findViewById<TextView>(R.id.myBykes_text) 
        myBikesActivity.setOnClickListener {

            val intent = Intent(activity, MyBikesActivity::class.java)
            startActivity(intent)
        }
        val myPointHistoryActivity = view.findViewById<TextView>(R.id.myPointHistory_text)
        myPointHistoryActivity.setOnClickListener {
            val intent = Intent(activity, MyPointHistoryActivity::class.java)
            startActivity(intent)
        }
        val helpActivity = view.findViewById<TextView>(R.id.help_text)
        helpActivity.setOnClickListener {
            val intent = Intent(activity, HelpActivity::class.java)
            startActivity(intent)
        }

        val versionText = view.findViewById<TextView>(R.id.version_text)
        versionText.text = "Version - " + this.activity?.applicationContext?.let { AppGlobal.GetAppVersion(it) }

        return  view
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // TODO: Use the ViewModel
    }
}
