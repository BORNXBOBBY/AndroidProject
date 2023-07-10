package com.example.registration

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rowView= inflater.inflate(R.layout.fragment_profile, container, false)
        val sharePreference=this.getActivity()?.getSharedPreferences("userPref",Context.MODE_PRIVATE)
        val  name=rowView.findViewById<TextView>(R.id.names)
        val mobile=rowView.findViewById<TextView>(R.id.mobile)
        val email=rowView.findViewById<TextView>(R.id.email)
        val password=rowView.findViewById<TextView>(R.id.password)

        name.text=sharePreference?.getString("name","")
        mobile.text=sharePreference?.getString("mobile","")
        email.text=sharePreference?.getString("email","")
        password.text=sharePreference?.getString("password","")
        return rowView
    }
}