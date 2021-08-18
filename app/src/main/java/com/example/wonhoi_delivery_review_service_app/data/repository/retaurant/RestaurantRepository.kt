package com.example.wonhoi_delivery_review_service_app.data.repository.retaurant

import com.example.wonhoi_delivery_review_service_app.data.entity.LocationLatLngEntity
import com.example.wonhoi_delivery_review_service_app.data.entity.RestaurantEntity
import com.example.wonhoi_delivery_review_service_app.screen.main.home.restaurant.RestaurantCategory

interface RestaurantRepository {

    suspend fun getList(
        restaurantCategory: RestaurantCategory,
        locationLatLngEntity: LocationLatLngEntity
    ) : List<RestaurantEntity>

}