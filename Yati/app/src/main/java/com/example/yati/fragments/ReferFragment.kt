package com.example.yati.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.yati.R
import com.example.yati.global.AppGlobal
import com.google.firebase.dynamiclinks.ktx.androidParameters
import com.google.firebase.dynamiclinks.ktx.dynamicLink
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.dynamiclinks.ktx.iosParameters
import com.google.firebase.ktx.Firebase

class ReferFragment : Fragment() {
    lateinit var inviteButton:Button
    val uid = AppGlobal.userid
    companion object {
        fun newInstance() = ReferFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rowView=inflater.inflate(R.layout.refer_fragment, container, false)
        inviteButton=rowView.findViewById(R.id.invite_button)
        return rowView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        inviteButton.setOnClickListener {
            ShareLink()
        }

    }

    private fun ShareLink() {
        Log.e("main", "crete link")
        val dynamicLink = Firebase.dynamicLinks.dynamicLink {
            link = Uri.parse("https://www.edugaon.com/")
            domainUriPrefix = "yatiyamahamobile.page.link"
            androidParameters { }
            iosParameters("com.example.ios") { }
        }

        val dynamicLinkUri = dynamicLink.uri
        Log.e("main", "long refer " + dynamicLinkUri.toString())
        //  https://yati.page.link?apn=com.example.yati&ibi=com.example.ios&link=https%3A%2F%2Fwww.edugaon.com%2F
        val intent= Intent()
        intent.setAction(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_TEXT,dynamicLinkUri.toString() + "http://edugaonlabs.com/" + uid)
        intent.setType("text/plain")
        startActivity(intent)
    }

}