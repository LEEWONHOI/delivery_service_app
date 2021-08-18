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
        supportFragmentManager.fragments.forEach { fragment ->  // 다 숨기고 아래에서 show 진행
            supportFragmentManager.beginTransaction().hide(fragment).commitAllowingStateLoss()
        }
        findFragment?.let {
            supportFragmentManager.beginTransaction().show(it).commitAllowingStateLoss()  // show 를 통해 기존에 fragment 가 있으면 해당 fragment 를 보여준다.
        } ?: kotlin.run {
            supportFragmentManager.beginTransaction()   // 없으면 새로 추가
                .add(R.id.fragmentContainer, fragment, tag)
                .commitAllowingStateLoss()  // 전환, 재시작 등의 이유로 onPause -> onSaveInstanceState() 이후 commit 작동으로 인한 손실 허용
        }
    }



}


enum class MainTabMenu(@IdRes val menuId : Int) {

    HOME(R.id.menu_home),
    LIKE(R.id.menu_Like),
    MY(R.id.menu_my)

}