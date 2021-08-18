package com.example.wonhoi_delivery_review_service_app.data.repository.retaurant.review

interface RestaurantReviewRepository {

    suspend fun getReviews(restaurantTitle : String) : DefaultRestaurantReviewRepository.Result

}