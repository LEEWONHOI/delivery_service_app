package com.example.wonhoi_delivery_review_service_app.screen.mylocation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.wonhoi_delivery_review_service_app.R
import com.example.wonhoi_delivery_review_service_app.data.entity.LocationLatLngEntity
import com.example.wonhoi_delivery_review_service_app.data.entity.MapSearchInfoEntity
import com.example.wonhoi_delivery_review_service_app.data.repository.map.MapRepository
import com.example.wonhoi_delivery_review_service_app.data.repository.user.UserRepository
import com.example.wonhoi_delivery_review_service_app.screen.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MyLocationViewModel(
   private val mapSearchInfoEntity: MapSearchInfoEntity,
   private val mapRepository: MapRepository,
   private val userRepository : UserRepository
) : BaseViewModel() {

   val myLocationStateLiveData = MutableLiveData<MyLocationState>(MyLocationState.Uninitialized)

   override fun fetchData(): Job = viewModelScope.launch {
      myLocationStateLiveData.value = MyLocationState.Loading
      myLocationStateLiveData.value = MyLocationState.Success(
         mapSearchInfoEntity
      )
   }

   fun changeLocationInfo(
      locationLatLngEntity: LocationLatLngEntity
   ) = viewModelScope.launch {
      val addressInfo = mapRepository.getReverseGeoInformation(locationLatLngEntity)
      addressInfo?.let { addressInfo ->
         myLocationStateLiveData.value = MyLocationState.Success(
            mapSearchInfoEntity = addressInfo.toSearchInfoEntity(locationLatLngEntity)
         )
      } ?: kotlin.run {
         myLocationStateLiveData.value = MyLocationState.Error(
            R.string.can_not_load_address_info
         )
      }
   }

   fun confirmSelectLocation() = viewModelScope.launch {
      when(val data = myLocationStateLiveData.value) {
         is MyLocationState.Success -> {

            userRepository.insertUserLocation(data.mapSearchInfoEntity.locationLatLngEntity)

            myLocationStateLiveData.value = MyLocationState.Confirm(
               data.mapSearchInfoEntity   // success 에서 받아온 mapSearchInfoEntity 정보  전달
            )
         }
      }
   }

}