package com.example.wonhoi_delivery_review_service_app.screen.main.home.restaurant.detail.review

import com.example.wonhoi_delivery_review_service_app.model.restaurant.review.RestaurantReviewModel

sealed class RestaurantReviewState {

    object Uninitialized : RestaurantReviewState()

    object Loading : RestaurantReviewState()

    data class Success(
        val reviewList: List<RestaurantReviewModel>
    ) : RestaurantReviewState()




}
