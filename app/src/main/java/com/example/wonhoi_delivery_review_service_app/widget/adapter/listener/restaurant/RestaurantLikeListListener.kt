package com.example.wonhoi_delivery_review_service_app.widget.adapter.listener.restaurant

import com.example.wonhoi_delivery_review_service_app.model.restaurant.RestaurantModel

interface RestaurantLikeListListener : RestaurantListListener {

    fun onDisLikeItem(model : RestaurantModel)


}