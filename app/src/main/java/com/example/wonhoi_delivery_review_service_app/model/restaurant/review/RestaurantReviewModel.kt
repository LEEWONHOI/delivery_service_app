package com.example.wonhoi_delivery_review_service_app.model.restaurant.review

import android.net.Uri
import com.example.wonhoi_delivery_review_service_app.model.CellType
import com.example.wonhoi_delivery_review_service_app.model.Model

data class RestaurantReviewModel(
    override val id: Long,
    override val type: CellType = CellType.REVIEW_CELL,
    val title : String,
    val description : String,
    val grade : Float,
    val thumbnailImageUri : Uri? = null
) : Model(id, type)
