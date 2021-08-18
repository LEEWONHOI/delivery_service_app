package com.example.wonhoi_delivery_review_service_app.data.entity

import android.os.Parcelable
import androidx.room.Dao
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.wonhoi_delivery_review_service_app.screen.main.home.restaurant.RestaurantCategory
import com.example.wonhoi_delivery_review_service_app.util.convertor.RoomTypeConverters
import kotlinx.parcelize.Parcelize

@Parcelize
@androidx.room.Entity
@TypeConverters(RoomTypeConverters::class)   // Pair 저장용
data class RestaurantEntity(
    override val id: Long,          // 모델용 고유 ID
    val restaurantInfoId : Long,    // Api용 아이디
    val restaurantCategory : RestaurantCategory,
    @PrimaryKey val restaurantTitle : String,
    val restaurantImageUrl : String,
    val grade : Float,
    val reviewCount : Int,
    val deliveryTimeRange : Pair<Int, Int>,
    val deliveryTipRange : Pair<Int, Int>,
    val restaurantTelNumber : String?
) : Entity, Parcelable
