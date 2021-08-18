package com.example.wonhoi_delivery_review_service_app.screen.main.like

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.wonhoi_delivery_review_service_app.data.entity.RestaurantEntity
import com.example.wonhoi_delivery_review_service_app.data.repository.user.UserRepository
import com.example.wonhoi_delivery_review_service_app.model.CellType
import com.example.wonhoi_delivery_review_service_app.model.restaurant.RestaurantModel
import com.example.wonhoi_delivery_review_service_app.screen.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class RestaurantLikeListViewModel(
    private val userRepository: UserRepository
) : BaseViewModel() {

    val restaurantListLiveData = MutableLiveData<List<RestaurantModel>>()

    override fun fetchData(): Job = viewModelScope.launch {
       restaurantListLiveData.value = userRepository.getAllUserLikeRestaurantList().map {
           RestaurantModel(
               id = it.id,
               type = CellType.LIKE_RESTAURANT_CELL,
               restaurantInfoId = it.restaurantInfoId,
               restaurantCategory = it.restaurantCategory,
               restaurantTitle = it.restaurantTitle,
               restaurantImageUrl = it.restaurantImageUrl,
               grade = it.grade,
               reviewCount = it.reviewCount,
               deliveryTimeRange = it.deliveryTimeRange,
               deliveryTipRange = it.deliveryTipRange,
               restaurantTelNumber = it.restaurantTelNumber,
           )
       }
    }

    fun dislikeRestaurant(restaurant: RestaurantEntity) = viewModelScope.launch {
        userRepository.deleteUserLikedRestaurant(restaurant.restaurantTitle)
        fetchData()
    }
}