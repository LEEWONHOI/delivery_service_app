package com.example.wonhoi_delivery_review_service_app.widget.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.wonhoi_delivery_review_service_app.data.entity.LocationLatLngEntity
import com.example.wonhoi_delivery_review_service_app.screen.main.home.restaurant.RestaurantListFragment

class RestaurantListFragmentPageAdapter(
    fragment : Fragment,
    val fragmentList : List<RestaurantListFragment>,
    var locationLatLngEntity: LocationLatLngEntity
    // FragmentStateAdapter 는 attach 될 때 Fragment 의 savedInstanceState 만 저장하고 Fragment 가 보이지 않을때 (포커스를 잃을때) destroy 시켜버린다.
) : FragmentStateAdapter(fragment) {


    override fun getItemCount(): Int = fragmentList.size

    override fun createFragment(position: Int): Fragment = fragmentList[position]

}