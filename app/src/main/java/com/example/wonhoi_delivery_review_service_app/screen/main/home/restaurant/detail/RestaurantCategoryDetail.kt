package com.example.wonhoi_delivery_review_service_app.screen.main.home.restaurant.detail

import androidx.annotation.StringRes
import com.example.wonhoi_delivery_review_service_app.R

enum class RestaurantCategoryDetail(
    @StringRes val categoryNameId : Int,
) {
    MENU(R.string.menu),
    REVIEW(R.string.review)
}