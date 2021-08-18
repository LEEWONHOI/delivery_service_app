package com.example.wonhoi_delivery_review_service_app.widget.adapter.listener.restaurant

import com.example.wonhoi_delivery_review_service_app.model.restaurant.food.FoodModel
import com.example.wonhoi_delivery_review_service_app.widget.adapter.listener.AdapterListener

interface FoodMenuListListener : AdapterListener {

    fun onClickItem(model: FoodModel)

}