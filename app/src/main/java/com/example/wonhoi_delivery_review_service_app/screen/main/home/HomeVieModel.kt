package com.example.wonhoi_delivery_review_service_app.screen.main.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.wonhoi_delivery_review_service_app.R
import com.example.wonhoi_delivery_review_service_app.data.entity.LocationLatLngEntity
import com.example.wonhoi_delivery_review_service_app.data.entity.MapSearchInfoEntity
import com.example.wonhoi_delivery_review_service_app.data.entity.RestaurantFoodEntity
import com.example.wonhoi_delivery_review_service_app.data.repository.map.MapRepository
import com.example.wonhoi_delivery_review_service_app.data.repository.retaurant.food.RestaurantFoodRepository
import com.example.wonhoi_delivery_review_service_app.data.repository.user.UserRepository
import com.example.wonhoi_delivery_review_service_app.screen.base.BaseViewModel
import kotlinx.coroutines.launch

class HomeVieModel(
    private val mapRepository: MapRepository,
    private val userRepository: UserRepository,
    private val restaurantFoodRepository: RestaurantFoodRepository
) : BaseViewModel() {

    val homeStateLiveData = MutableLiveData<HomeState>(HomeState.Uninitialize)

    val foodMenuBasketLiveData = MutableLiveData<List<RestaurantFoodEntity>>()

    fun loadReverseGeoInformation(
        locationLatLngEntity: LocationLatLngEntity
    ) = viewModelScope.launch {
        homeStateLiveData.value = HomeState.Loading
        // 현재 위치
        val userLocation = userRepository.getUserLocation()
        val currentLocation = userLocation ?: locationLatLngEntity

        // 유저 위치 있으면 currentLocation , 없으면 원래 디바이스에서 받아온 locationLatLngEntity
        val addressInfo = mapRepository.getReverseGeoInformation(currentLocation)
        addressInfo?.let { addressInfo ->
            homeStateLiveData.value = HomeState.Success(
                mapSearchInfo = addressInfo.toSearchInfoEntity(locationLatLngEntity),
                isLocationSame = currentLocation == locationLatLngEntity
            )
        } ?: kotlin.run {
            homeStateLiveData.value = HomeState.Error(
                R.string.can_not_load_address_info
            )
        }
    }

    fun getMapSearchInfo(): MapSearchInfoEntity? {
        when (val data = homeStateLiveData.value) {
            is HomeState.Success -> {
                return data.mapSearchInfo
            }
        }
        return null
    }

    fun checkMyBasket() = viewModelScope.launch {
      foodMenuBasketLiveData.value  = restaurantFoodRepository.getAllFoodMenuListInBasket()
    }

    companion object {
        const val MY_LOCATION_KEY = "MyLocation"
    }


}