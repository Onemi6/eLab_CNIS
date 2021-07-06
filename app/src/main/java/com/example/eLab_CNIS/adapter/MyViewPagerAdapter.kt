package com.example.eLab_CNIS.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import java.util.ArrayList

class MyViewPagerAdapter : FragmentPagerAdapter {

    private var mTitles: List<String>? = null
    private var mFragments: MutableList<Fragment> = ArrayList()

    constructor(fm: FragmentManager?, titles: List<String>?) : super(fm!!) {
        mTitles = titles
    }

    constructor(fm: FragmentManager?) : super(fm!!)

    fun addFragment(fragment: Fragment) {
        mFragments.add(fragment)
    }

    override fun getItem(position: Int): Fragment {
        return mFragments[position]
    }

    override fun getCount(): Int {
        return mFragments.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mTitles!![position]
    }
}