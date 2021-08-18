package com.example.wonhoi_delivery_review_service_app.model.restaurant.order

import com.example.wonhoi_delivery_review_service_app.data.entity.OrderEntity
import com.example.wonhoi_delivery_review_service_app.data.entity.RestaurantFoodEntity
import com.example.wonhoi_delivery_review_service_app.model.CellType
import com.example.wonhoi_delivery_review_service_app.model.Model

data class OrderModel (
    override val id: Long,
    override val type: CellType = CellType.ORDER_CELL,
    val orderId : String,
    val userId : String,
    val restaurantId : Long,
    val foodMenuList : List<RestaurantFoodEntity>,
    val restaurantTitle : String
) : Model(id, type) {

    fun toEntity() = OrderEntity(
        id = orderId,
        userId = userId,
        restaurantId = restaurantId,
        foodMenuList = foodMenuList,
        restaurantTitle = restaurantTitle
    )

}
