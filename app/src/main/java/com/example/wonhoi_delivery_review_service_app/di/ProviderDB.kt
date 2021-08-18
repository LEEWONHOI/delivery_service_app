package com.example.wonhoi_delivery_review_service_app.di

import android.content.Context
import androidx.room.Room
import com.example.wonhoi_delivery_review_service_app.data.db.ApplicationDatabase

fun provideDB(context: Context) : ApplicationDatabase =
    Room.databaseBuilder(
        context,
        ApplicationDatabase::class.java,
        ApplicationDatabase.DB_NAME
    ).build()


fun provideLocationDao(database: ApplicationDatabase) = database.LocationDao()

fun provideRestaurantDao(database: ApplicationDatabase) = database.RestaurantDao()

fun provideFoodMenuBasketDao(database: ApplicationDatabase) = database.FoodMenuBasketDao()
