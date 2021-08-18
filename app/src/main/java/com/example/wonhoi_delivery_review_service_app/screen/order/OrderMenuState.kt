package com.example.wonhoi_delivery_review_service_app.screen.order

import androidx.annotation.StringRes
import com.example.wonhoi_delivery_review_service_app.model.restaurant.food.FoodModel

sealed class OrderMenuState {

    object Uninitialized : OrderMenuState()

    object Loading : OrderMenuState()

    data class Success(
        val restaurantFoodModelList : List<FoodModel>? = null
    ) : OrderMenuState()

    object Order : OrderMenuState()

    data class Error(
        @StringRes val messageId : Int,
        val e : Throwable
    ) : OrderMenuState()

}