package com.example.wonhoi_delivery_review_service_app.extensions

import android.content.res.Resources

fun Float.fromDpToPx() : Int {
    return (this * Resources.getSystem().displayMetrics.density).toInt()
}