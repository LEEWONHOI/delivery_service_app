package com.example.wonhoi_delivery_review_service_app.data.entity

import android.os.Parcelable
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@androidx.room.Entity
data class LocationLatLngEntity(
    val latitude : Double,
    val longitude : Double,
    @PrimaryKey(autoGenerate = true)
    override val id: Long = -1
    ) : Entity, Parcelable