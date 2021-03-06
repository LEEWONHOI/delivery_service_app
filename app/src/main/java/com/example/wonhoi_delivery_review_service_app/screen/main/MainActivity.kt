package com.example.wonhoi_delivery_review_service_app.screen.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.wonhoi_delivery_review_service_app.R
import com.example.wonhoi_delivery_review_service_app.databinding.ActivityMainBinding
import com.example.wonhoi_delivery_review_service_app.screen.main.home.HomeFragment
import com.example.wonhoi_delivery_review_service_app.screen.main.like.RestaurantLikeListFragment
import com.example.wonhoi_delivery_review_service_app.screen.main.my.MyFragment
import com.example.wonhoi_delivery_review_service_app.util.event.MenuChangeEventBus
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding : ActivityMainBinding

    private val menuChangeEventBus by inject<MenuChangeEventBus>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observeData()

        initView()
    }

    fun goToTab(mainTabMenu: MainTabMenu) {
        binding.bottomNav.selectedItemId = mainTabMenu.menuId
    }

    private fun observeData() {
        lifecycleScope.launch {
            menuChangeEventBus.mainTabMenuFlow.collect {
                goToTab(it)
            }
        }
    }

    private fun initView() = with(binding) {
        bottomNav.setOnNavigationItemSelectedListener(this@MainActivity)    // <- onNavigationItemSelected
        showFragment(HomeFragment.newInstance(), HomeFragment.TAG )
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {    // <- BottomNavigationView.OnNavigationItemSelectedListener
        return when(item.itemId) {
            R.id.menu_home -> {
                showFragment(HomeFragment.newInstance(), HomeFragment.TAG)
                true
            }
            R.id.menu_my -> {
                showFragment(MyFragment.newInstance(), MyFragment.TAG)
                true
            }
            R.id.menu_Like -> {
                showFragment(RestaurantLikeListFragment(), RestaurantLikeListFragment.TAG)
                true
            }
            else -> false
        }
    }



    private fun showFragment(fragment : Fragment, tag: String) {
        val findFragment = supportFragmentManager.findFragmentByTag(tag)
        supportFragmentManager.fragments.forEach { fragment ->  // ??? ????????? ???????????? show ??????
            supportFragmentManager.beginTransaction().hide(fragment).commitAllowingStateLoss()
        }
        findFragment?.let {
            supportFragmentManager.beginTransaction().show(it).commitAllowingStateLoss()  // show ??? ?????? ????????? fragment ??? ????????? ?????? fragment ??? ????????????.
        } ?: kotlin.run {
            supportFragmentManager.beginTransaction()   // ????????? ?????? ??????
                .add(R.id.fragmentContainer, fragment, tag)
                .commitAllowingStateLoss()  // ??????, ????????? ?????? ????????? onPause -> onSaveInstanceState() ?????? commit ???????????? ?????? ?????? ??????
        }
    }



}


enum class MainTabMenu(@IdRes val menuId : Int) {

    HOME(R.id.menu_home),
    LIKE(R.id.menu_Like),
    MY(R.id.menu_my)

}