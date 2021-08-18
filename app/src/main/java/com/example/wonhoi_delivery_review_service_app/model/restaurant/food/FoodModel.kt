package com.example.wonhoi_delivery_review_service_app.model.restaurant.food

import com.example.wonhoi_delivery_review_service_app.data.entity.RestaurantFoodEntity
import com.example.wonhoi_delivery_review_service_app.model.CellType
import com.example.wonhoi_delivery_review_service_app.model.Model

data class FoodModel(
    override val id: Long,
    override val type: CellType = CellType.FOOD_CELL,
    val title: String,
    val description: String,
    val price: Int,
    val imageUrl: String,
    val restaurantId: Long,
    val foodId : String,
    val restaurantTitle: String
) : Model(id, type) {

    fun toEntity(basketIndex : Int) = RestaurantFoodEntity(
        "${foodId}_${basketIndex}",
        title = title,
        description = description,
        price = price,
        imageUrl = imageUrl,
        restaurantId = restaurantId,
        restaurantTitle = restaurantTitle
    )



}
