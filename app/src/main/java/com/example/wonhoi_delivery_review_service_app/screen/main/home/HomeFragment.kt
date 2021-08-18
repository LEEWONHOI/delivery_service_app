package com.example.wonhoi_delivery_review_service_app.screen.main.home

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.example.wonhoi_delivery_review_service_app.R
import com.example.wonhoi_delivery_review_service_app.data.entity.LocationLatLngEntity
import com.example.wonhoi_delivery_review_service_app.data.entity.MapSearchInfoEntity
import com.example.wonhoi_delivery_review_service_app.databinding.FragmentHomeBinding
import com.example.wonhoi_delivery_review_service_app.screen.base.BaseFragment
import com.example.wonhoi_delivery_review_service_app.screen.main.MainActivity
import com.example.wonhoi_delivery_review_service_app.screen.main.MainTabMenu
import com.example.wonhoi_delivery_review_service_app.screen.main.home.restaurant.RestaurantCategory
import com.example.wonhoi_delivery_review_service_app.screen.main.home.restaurant.RestaurantListFragment
import com.example.wonhoi_delivery_review_service_app.screen.main.home.restaurant.RestaurantOrder
import com.example.wonhoi_delivery_review_service_app.screen.mylocation.MyLocationActivity
import com.example.wonhoi_delivery_review_service_app.screen.order.OrderMenuListActivity
import com.example.wonhoi_delivery_review_service_app.widget.adapter.RestaurantListFragmentPageAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment<HomeVieModel, FragmentHomeBinding>() {

    override val viewModel by viewModel<HomeVieModel>()

    override fun getViewBinding(): FragmentHomeBinding = FragmentHomeBinding.inflate(layoutInflater)

    private lateinit var viewPageAdapter: RestaurantListFragmentPageAdapter

    private lateinit var locationManager: LocationManager

    private lateinit var myLocationListener: MyLocationListener

    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }

    private val changeLocationLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { activityResult ->
        if (activityResult.resultCode == Activity.RESULT_OK) {
            activityResult.data?.getParcelableExtra<MapSearchInfoEntity>(HomeVieModel.MY_LOCATION_KEY)
                ?.let { myLocationInfo ->
                    viewModel.loadReverseGeoInformation(myLocationInfo.locationLatLngEntity)
                }
        }
    }

    private val locationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permission -> // Map <String, Boolean>
            val responsePermissions = permission.entries.filter {
                (it.key == Manifest.permission.ACCESS_FINE_LOCATION)
                        || (it.key == Manifest.permission.ACCESS_COARSE_LOCATION)  // 키값 검사
            }
            if (responsePermissions.filter { it.value == true }.size == locationPermissions.size) { // 벨류 및 총 허가된 퍼미션 검사
                setMyLocationListener() // 위치 정보 set
            } else {    // 권한 재요청
                with(binding.locationTitleText) {
                    setText(R.string.please_request_location_permission)
                    setOnClickListener {
                        getMyLocation()
                    }
                }
                Toast.makeText(
                    requireContext(),
                    R.string.can_not_assigned_permission,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }


    private fun initViewPager(locationLatLng: LocationLatLngEntity) = with(binding) {
        val restaurantCategories = RestaurantCategory.values()

        if (::viewPageAdapter.isInitialized.not()) {
            orderChipGroup.isVisible = true
            // enum RestaurantCategory 에서 선언한 식당 카테고리 데이터를 전부 꺼내와서 전달하여 프레그먼트를 만든다.
            val restaurantListFragmentList = restaurantCategories.map { restaurantCategories ->
                RestaurantListFragment.newInstance(restaurantCategories, locationLatLng)
            }

            viewPageAdapter = RestaurantListFragmentPageAdapter(
                this@HomeFragment,
                restaurantListFragmentList,
                locationLatLng
            )
            viewPager.adapter = viewPageAdapter
            //한번 만들어진 페이지를 다시 만들지않고 유지시킴
            viewPager.offscreenPageLimit = restaurantCategories.size
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.setText(restaurantCategories[position].categoryNameId)
            }.attach()
        }
        if (locationLatLng != viewPageAdapter.locationLatLngEntity) {
            viewPageAdapter.locationLatLngEntity = locationLatLng
            viewPageAdapter.fragmentList.forEach {
                it.viewModel.setLocationLatLng(locationLatLng)
            }
        }
    }

    override fun onResume() {
        super.onResume()

        viewModel.checkMyBasket()
    }

    override fun observeData() {
        viewModel.homeStateLiveData.observe(viewLifecycleOwner) { homeState ->
            when (homeState) {
                is HomeState.Uninitialize -> {
                    getMyLocation()
                }
                is HomeState.Loading -> {
                    binding.locationLoading.isVisible = true
                    binding.locationTitleText.text = getString(R.string.loading)
                }
                is HomeState.Success -> {
                    binding.locationLoading.isGone = true
                    binding.locationTitleText.text = homeState.mapSearchInfo.fullAddress
                    binding.tabLayout.isVisible = true
                    binding.filterScrollView.isVisible = true
                    binding.viewPager.isVisible = true

                    initViewPager(homeState.mapSearchInfo.locationLatLngEntity)
                    if (homeState.isLocationSame.not()) {
                        Toast.makeText(
                            requireContext(),
                            R.string.please_set_your_current_location,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                is HomeState.Error -> {
                    binding.locationLoading.isGone = true
                    binding.locationTitleText.text = getString(R.string.can_not_load_address_info)
                    binding.locationTitleText.setOnClickListener {
                        getMyLocation()
                    }
                    Toast.makeText(requireContext(), homeState.messageId, Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.foodMenuBasketLiveData.observe(this) {
            if (it.isNotEmpty()) {
                binding.basketButtonContainer.isVisible = true
                binding.basketCountTextView.text = getString(R.string.basket_count, it.size)
                binding.basketButton.setOnClickListener {
                    // 주문화면, 로그인 화면으로 이동하기
                    if(firebaseAuth.currentUser == null) {
                        alertLoginNeed {
                            (requireActivity() as MainActivity).goToTab(MainTabMenu.MY) // afterAction
                        }
                    } else {
                        // 장바구니 이동
                        startActivity(
                            OrderMenuListActivity.newIntent(requireContext())
                        )
                    }
                }
            } else { // 장바구니 비어있음음
                binding.basketButtonContainer.isGone = true
                binding.basketButtonContainer.setOnClickListener(null)
            }
        }
    }

    private fun alertLoginNeed(afterAction: () -> Unit) {
        AlertDialog.Builder(requireContext())
            .setTitle("Login is required.")
            .setMessage("Login is required to order. Are you sure you want to go to the My tab?")
            .setPositiveButton("move") { dialog, _ ->
                afterAction()
                dialog.dismiss()
            }
            .setNegativeButton("cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    override fun initViews() = with(binding) {
        locationTitleText.setOnClickListener {
            viewModel.getMapSearchInfo()?.let { mapSearchInfoEntity ->
                changeLocationLauncher.launch(
                    MyLocationActivity.newIntent(
                        requireContext(), mapSearchInfoEntity
                    )
                )
            }
        }
        // Chip 클릭 시 화면 다시 뿌려주기
        orderChipGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.chipDefault -> {
                    chipInitialize.isGone = true
                    changeRestaurantOrder(RestaurantOrder.DEFAULT)
                }
                R.id.chipInitialize -> {
                    chipDefault.isChecked = true
                }
                R.id.chipLowDeliveryTip -> {
                    chipInitialize.isVisible = true
                    changeRestaurantOrder(RestaurantOrder.LOW_DELIVERY_TIP)
                }
                R.id.chipFastDelivery -> {
                    chipInitialize.isVisible = true
                    changeRestaurantOrder(RestaurantOrder.FAST_DELIVERY)
                }
                R.id.chipTopRate -> {
                    chipInitialize.isVisible = true
                    changeRestaurantOrder(RestaurantOrder.TOP_LATE)
                }
            }
        }
    }

    private fun changeRestaurantOrder(order: RestaurantOrder) {
        viewPageAdapter.fragmentList.forEach {
            it.viewModel.setRestaurantOrder(order)
        }
    }


    private fun getMyLocation() {
        if (::locationManager.isInitialized.not()) {
            locationManager =
                requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        }
        val isGpsUnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (isGpsUnabled) {
            locationPermissionLauncher.launch(locationPermissions)
        }
    }

    @SuppressLint("MissingPermission")
    private fun setMyLocationListener() {
        val minTime = 1500L
        val minDistance = 100f
        if (::myLocationListener.isInitialized.not()) {
            myLocationListener = MyLocationListener()
        } // 위치정보 리스너 생성
        with(locationManager) {
            requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                minTime, minDistance, myLocationListener
            )
            requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                minTime, minDistance, myLocationListener
            )
        }
    }

    companion object {

        val locationPermissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        fun newInstance() = HomeFragment()

        const val TAG = "HomeFragment"
    }

    private fun removeLocationListener() {
        if (::locationManager.isInitialized && ::myLocationListener.isInitialized) {
            locationManager.removeUpdates(myLocationListener)
        }
    }

    inner class MyLocationListener : LocationListener {
        override fun onLocationChanged(location: Location) {
            //   binding.locationTitleText.text = "${location.latitude}, ${location.longitude}"
            viewModel.loadReverseGeoInformation(
                LocationLatLngEntity(
                    location.latitude,
                    location.longitude,
                )
            )
            removeLocationListener()    // 위치정보 변경에 따른 반복 요청 방지. 요청 시 1회만
        }
    }

}