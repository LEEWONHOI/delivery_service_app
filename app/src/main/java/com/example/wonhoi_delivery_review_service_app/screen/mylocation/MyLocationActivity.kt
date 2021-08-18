package com.example.wonhoi_delivery_review_service_app.screen.mylocation

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.example.wonhoi_delivery_review_service_app.R
import com.example.wonhoi_delivery_review_service_app.data.entity.LocationLatLngEntity
import com.example.wonhoi_delivery_review_service_app.data.entity.MapSearchInfoEntity
import com.example.wonhoi_delivery_review_service_app.databinding.ActivityMyLocationBinding
import com.example.wonhoi_delivery_review_service_app.screen.base.BaseActivity
import com.example.wonhoi_delivery_review_service_app.screen.main.home.HomeVieModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class MyLocationActivity : BaseActivity<MyLocationViewModel, ActivityMyLocationBinding>(),
    OnMapReadyCallback {

    companion object {
        const val CAMERA_ZOOM_LEVEL = 17f

        fun newIntent(context: Context, mapSearchInfoEntity: MapSearchInfoEntity) =
            Intent(context, MyLocationActivity::class.java).apply {
                putExtra(
                    HomeVieModel.MY_LOCATION_KEY,
                    mapSearchInfoEntity
                ) // HomeVieModel 에서 측정한 내 위치정보 전달 받음
            }
    }

    override val viewModel by viewModel<MyLocationViewModel> {
        parametersOf(   // MyLocationViewModel 에서 MY_LOCATION_KEY 키값을 기반으로 인텐트를 가져옴
            intent.getParcelableExtra<MapSearchInfoEntity>(HomeVieModel.MY_LOCATION_KEY)
        )
    }

    override fun getViewBinding(): ActivityMyLocationBinding =
        ActivityMyLocationBinding.inflate(layoutInflater)

    private lateinit var map: GoogleMap

    private var isMapInitialized: Boolean = false
    private var isChangeLocation: Boolean = false

    override fun onMapReady(map: GoogleMap) {
        this.map = map ?: return
        viewModel.fetchData()
    }

    override fun initViews() = with(binding) {
        toolbar.setNavigationOnClickListener {
            finish()
        }
        confirmButton.setOnClickListener {
            viewModel.confirmSelectLocation()
        }
        setupGoogleMap()
    }

    private fun setupGoogleMap() {
        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun observeDate() =
        viewModel.myLocationStateLiveData.observe(this) { myLocationState ->
            when (myLocationState) {
                is MyLocationState.Loading -> {
                    handleLoadingState()
                }
                is MyLocationState.Success -> {
                    if (::map.isInitialized) {
                        handleSuccessState(myLocationState)
                    }
                }
                is MyLocationState.Confirm -> { // 버튼 클릭 시 컨펌 상태로 넘어감
                    setResult(Activity.RESULT_OK, Intent().apply {
                        putExtra(HomeVieModel.MY_LOCATION_KEY, myLocationState.mapSearchInfoEntity) // 키와 새로 가져온 위치정보를 보냄
                    })
                    finish()
                }
                is MyLocationState.Error -> {
                    Toast.makeText(this, myLocationState.messageId, Toast.LENGTH_SHORT).show()
                }
                else -> Unit
            }
        }

    private fun handleLoadingState() = with(binding) {
        locationLoading.isVisible = true
        locationTitleText.text = getString(R.string.loading)
    }

    private fun handleSuccessState(state: MyLocationState.Success) = with(binding) {
        val mapSearchInfo = state.mapSearchInfoEntity
        locationLoading.isGone = true
        locationTitleText.text = mapSearchInfo.fullAddress
        if (isMapInitialized.not()) {   // 맵 초기화
            map.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(
                        mapSearchInfo.locationLatLngEntity.latitude,
                        mapSearchInfo.locationLatLngEntity.longitude
                    ), CAMERA_ZOOM_LEVEL
                )
            )
            map.setOnCameraIdleListener { // 지도가 멈춰있는지 판단하는 리스너
                if (isChangeLocation.not()) {
                    isChangeLocation = true // 반복 호출 방지
                    Handler(Looper.getMainLooper()).postDelayed({
                        val cameraLatLng = map.cameraPosition.target
                        viewModel.changeLocationInfo(
                            LocationLatLngEntity(
                                cameraLatLng.latitude,
                                cameraLatLng.longitude
                            )
                        )
                        isChangeLocation = false
                    }, 1000)
                }
            }
            isMapInitialized = true
        }
    }


}