package com.example.wonhoi_delivery_review_service_app.widget.adapter.listener.order

import com.example.wonhoi_delivery_review_service_app.widget.adapter.listener.AdapterListener

interface OrderListListener : AdapterListener {

    fun writeRestaurantReview(orderId : String, restaurantTitle : String)

}