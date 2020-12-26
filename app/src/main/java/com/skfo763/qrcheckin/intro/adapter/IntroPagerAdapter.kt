package com.skfo763.qrcheckin.intro.adapter

import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.skfo763.qrcheckin.intro.fragment.OtherSettingsFragment
import com.skfo763.qrcheckin.intro.fragment.PermissionFragment
import com.skfo763.qrcheckin.intro.fragment.QrCheckInSettingsFragment

class IntroPagerAdapter(activity: FragmentActivity): FragmentStateAdapter(activity) {
    private val fragmentList = listOf(
        PermissionFragment(),
        QrCheckInSettingsFragment()
    )

    fun getItem(position: Int) = fragmentList[position]

    override fun getItemCount() = fragmentList.size

    override fun createFragment(position: Int) = fragmentList[position]
}