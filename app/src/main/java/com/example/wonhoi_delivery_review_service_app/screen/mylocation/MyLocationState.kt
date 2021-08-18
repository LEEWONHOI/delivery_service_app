package com.example.wonhoi_delivery_review_service_app.screen.mylocation

import androidx.annotation.StringRes
import com.example.wonhoi_delivery_review_service_app.data.entity.MapSearchInfoEntity

sealed class MyLocationState {

    object Uninitialized : MyLocationState()

    object Loading : MyLocationState()

    data class Success(
        val mapSearchInfoEntity: MapSearchInfoEntity
    ) : MyLocationState()

    data class Confirm(
        val mapSearchInfoEntity: MapSearchInfoEntity
    ) : MyLocationState()

    data class Error(
        @StringRes val messageId: Int
    ) : MyLocationState()

}