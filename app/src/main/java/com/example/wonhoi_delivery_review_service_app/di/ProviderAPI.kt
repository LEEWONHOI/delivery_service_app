package com.example.wonhoi_delivery_review_service_app.di

import com.example.wonhoi_delivery_review_service_app.data.network.FoodApiService
import com.example.wonhoi_delivery_review_service_app.data.network.MapApiService
import com.example.wonhoi_delivery_review_service_app.data.url.Url
import com.google.firebase.ktx.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

fun provideMapApiService(retrofit: Retrofit) : MapApiService {
    return retrofit.create(MapApiService::class.java)
}

fun provideFoodApiService(retrofit: Retrofit) : FoodApiService {
    return retrofit.create(FoodApiService::class.java)
}

fun provideMapRetrofit(
    okHttpClient: OkHttpClient,
    gsonConverterFactory: GsonConverterFactory
) : Retrofit {
    return Retrofit.Builder()
        .baseUrl(Url.TMAP_URL)
        .addConverterFactory(gsonConverterFactory)
        .client(okHttpClient)
        .build()
}

fun provideFoodRetrofit(
    okHttpClient: OkHttpClient,
    gsonConverterFactory: GsonConverterFactory
) : Retrofit {
    return Retrofit.Builder()
        .baseUrl(Url.FOOD_URL)
        .addConverterFactory(gsonConverterFactory)
        .client(okHttpClient)
        .build()
}

fun provideGsonConvertFactory(): GsonConverterFactory {
    return GsonConverterFactory.create()
}

fun buildOkHttpClient(): OkHttpClient {
    val interceptor = HttpLoggingInterceptor()
    if (BuildConfig.DEBUG) {
        interceptor.level = HttpLoggingInterceptor.Level.BODY
    } else {
        interceptor.level = HttpLoggingInterceptor.Level.NONE
    }
    return OkHttpClient.Builder()
        .connectTimeout(5, TimeUnit.SECONDS)
        .addInterceptor(interceptor)
        .build()
}