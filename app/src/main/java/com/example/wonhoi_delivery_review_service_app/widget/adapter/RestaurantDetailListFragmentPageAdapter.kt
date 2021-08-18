package com.example.wonhoi_delivery_review_service_app.widget.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class RestaurantDetailListFragmentPageAdapter(
    activity : FragmentActivity,
    val fragmentList : List<Fragment>
) : FragmentStateAdapter(activity){

    override fun getItemCount(): Int = fragmentList.size

    override fun createFragment(position: Int): Fragment = fragmentList[position]
}