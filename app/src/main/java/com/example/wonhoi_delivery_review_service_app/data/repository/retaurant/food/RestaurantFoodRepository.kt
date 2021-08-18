package com.example.wonhoi_delivery_review_service_app.data.repository.retaurant.food

import com.example.wonhoi_delivery_review_service_app.data.entity.RestaurantFoodEntity

interface RestaurantFoodRepository {

    suspend fun getFoods(restaurantId: Long, restaurantTitle : String): List<RestaurantFoodEntity>

    suspend fun getAllFoodMenuListInBasket() : List<RestaurantFoodEntity>

    suspend fun getFoodMenuListInBasket(restaurantId: Long): List<RestaurantFoodEntity>

    suspend fun insertFoodMenuListInBasket(restaurantFoodEntity: RestaurantFoodEntity)

    suspend fun removeFoodMenuListInBasket(foodId: String)

    suspend fun clearFoodMenuListInBasket()

}