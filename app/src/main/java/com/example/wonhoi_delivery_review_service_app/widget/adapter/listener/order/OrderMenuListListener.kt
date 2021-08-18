package com.example.wonhoi_delivery_review_service_app.widget.adapter.listener.order

import com.example.wonhoi_delivery_review_service_app.model.restaurant.food.FoodModel
import com.example.wonhoi_delivery_review_service_app.widget.adapter.listener.AdapterListener

interface OrderMenuListListener : AdapterListener {

    fun onRemoveItem(foodModel: FoodModel)

}