package com.example.wonhoi_delivery_review_service_app.util.convertor

import androidx.room.TypeConverter

object RoomTypeConverters {

    @TypeConverter
    @JvmStatic
    fun toString(pair: Pair<Int, Int>): String {
        return "${pair.first},${pair.second}"
    }

    @TypeConverter
    @JvmStatic
    fun toIntPair(str: String): Pair<Int, Int> {
        val splitedStr = str.split(",")
        return Pair(Integer.parseInt(splitedStr[0]), Integer.parseInt(splitedStr[1]))
    }

}