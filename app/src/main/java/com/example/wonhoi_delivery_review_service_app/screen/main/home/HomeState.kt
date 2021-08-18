package com.example.wonhoi_delivery_review_service_app.screen.main.home

import androidx.annotation.StringRes
import com.example.wonhoi_delivery_review_service_app.data.entity.MapSearchInfoEntity

sealed class HomeState {

    object Uninitialize : HomeState()

    object Loading : HomeState()

    data class Success(
        val mapSearchInfo: MapSearchInfoEntity,
        val isLocationSame : Boolean

    ): HomeState()

    data class Error(
        @StringRes val messageId : Int
    ) : HomeState()

}