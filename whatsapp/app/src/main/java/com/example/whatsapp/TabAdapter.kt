package com.example.whatsapp

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class TabAdapter(supportFragmentManager: FragmentManager) : FragmentStatePagerAdapter(supportFragmentManager) {
    private val mFragmentlist = ArrayList<Fragment>()
    private val mFragmentTitleList = ArrayList<String>()
    fun addFragment(fragment:Fragment, title: String) {
        mFragmentlist.add(fragment)
        mFragmentTitleList.add(title)
    }

    override fun getCount(): Int {
        return mFragmentlist.size
    }

    override fun getItem(position: Int): Fragment {
        return mFragmentlist[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mFragmentTitleList[position]
    }
}