package com.example.wonhoi_delivery_review_service_app.data.repository.user

import android.icu.text.CaseMap
import com.example.wonhoi_delivery_review_service_app.data.entity.LocationLatLngEntity
import com.example.wonhoi_delivery_review_service_app.data.entity.RestaurantEntity

interface UserRepository {

    suspend fun getUserLocation() : LocationLatLngEntity?

    suspend fun insertUserLocation(locationLatLngEntity: LocationLatLngEntity)

    suspend fun getUserLikedRestaurant(restaurantTitle: String ) : RestaurantEntity?

    suspend fun getAllUserLikeRestaurantList() : List<RestaurantEntity>

    suspend fun insertUserLikeRestaurant(restaurantEntity: RestaurantEntity)

    suspend fun deleteUserLikedRestaurant(restaurantTitle: String)

    suspend fun deleteAllUserLikedRestaurant()

}