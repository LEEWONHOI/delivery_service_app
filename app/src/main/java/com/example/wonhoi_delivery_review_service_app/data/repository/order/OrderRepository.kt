package com.example.wonhoi_delivery_review_service_app.data.repository.order

import com.example.wonhoi_delivery_review_service_app.data.entity.RestaurantFoodEntity

interface OrderRepository {

    suspend fun orderMenu(
        userId : String,
        restaurantId : Long,
        foodMenuList : List<RestaurantFoodEntity>,
        restaurantTitle : String
    ) : DefaultOrderRepository.Result

    suspend fun getAllOrderMenus(
        userId: String
    ) : DefaultOrderRepository.Result

}