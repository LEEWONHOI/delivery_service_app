package com.example.wonhoi_delivery_review_service_app.di

import com.example.wonhoi_delivery_review_service_app.data.entity.LocationLatLngEntity
import com.example.wonhoi_delivery_review_service_app.data.entity.MapSearchInfoEntity
import com.example.wonhoi_delivery_review_service_app.data.entity.RestaurantEntity
import com.example.wonhoi_delivery_review_service_app.data.entity.RestaurantFoodEntity
import com.example.wonhoi_delivery_review_service_app.data.preference.AppPreferenceManager
import com.example.wonhoi_delivery_review_service_app.data.repository.map.DefaultMapRepository
import com.example.wonhoi_delivery_review_service_app.data.repository.map.MapRepository
import com.example.wonhoi_delivery_review_service_app.data.repository.order.DefaultOrderRepository
import com.example.wonhoi_delivery_review_service_app.data.repository.order.OrderRepository
import com.example.wonhoi_delivery_review_service_app.data.repository.retaurant.DefaultRestaurantRepository
import com.example.wonhoi_delivery_review_service_app.data.repository.retaurant.RestaurantRepository
import com.example.wonhoi_delivery_review_service_app.data.repository.retaurant.food.DefaultRestaurantFoodRepository
import com.example.wonhoi_delivery_review_service_app.data.repository.retaurant.food.RestaurantFoodRepository
import com.example.wonhoi_delivery_review_service_app.data.repository.retaurant.review.DefaultRestaurantReviewRepository
import com.example.wonhoi_delivery_review_service_app.data.repository.retaurant.review.RestaurantReviewRepository
import com.example.wonhoi_delivery_review_service_app.data.repository.user.DefaultUserRepository
import com.example.wonhoi_delivery_review_service_app.data.repository.user.UserRepository
import com.example.wonhoi_delivery_review_service_app.screen.main.home.HomeVieModel
import com.example.wonhoi_delivery_review_service_app.screen.main.home.restaurant.RestaurantCategory
import com.example.wonhoi_delivery_review_service_app.screen.main.home.restaurant.RestaurantListViewModel
import com.example.wonhoi_delivery_review_service_app.screen.main.home.restaurant.detail.RestaurantDetailViewModel
import com.example.wonhoi_delivery_review_service_app.screen.main.home.restaurant.detail.menu.RestaurantMenuListViewModel
import com.example.wonhoi_delivery_review_service_app.screen.main.home.restaurant.detail.review.RestaurantReviewListViewModel
import com.example.wonhoi_delivery_review_service_app.screen.main.like.RestaurantLikeListViewModel
import com.example.wonhoi_delivery_review_service_app.screen.main.my.MyViewModel
import com.example.wonhoi_delivery_review_service_app.screen.mylocation.MyLocationViewModel
import com.example.wonhoi_delivery_review_service_app.screen.order.OrderMenuListViewModel
import com.example.wonhoi_delivery_review_service_app.screen.review.gallery.GalleryPhotoRepository
import com.example.wonhoi_delivery_review_service_app.screen.review.gallery.GalleryViewModel
import com.example.wonhoi_delivery_review_service_app.util.event.MenuChangeEventBus
import com.example.wonhoi_delivery_review_service_app.util.provider.DefaultResourcesProvider
import com.example.wonhoi_delivery_review_service_app.util.provider.ResourcesProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidApplication
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appModule = module {

    // Dispatcher
    single { Dispatchers.IO }
    single { Dispatchers.Main }

    single<ResourcesProvider> { DefaultResourcesProvider(androidApplication()) }
    single { AppPreferenceManager(androidApplication()) }
    single { MenuChangeEventBus() }

    single { Firebase.firestore }
    single { Firebase.storage }
    single { FirebaseAuth.getInstance() }

    // Repository
    single<RestaurantRepository> { DefaultRestaurantRepository(get(), get(), get()) }
    single<MapRepository> { DefaultMapRepository(get(), get()) }
    single<UserRepository> { DefaultUserRepository(get(), get(),get()) }
    single<RestaurantFoodRepository> { DefaultRestaurantFoodRepository(get(), get(), get()) }
    single<RestaurantReviewRepository> { DefaultRestaurantReviewRepository(get(), get()) }
    single<OrderRepository> { DefaultOrderRepository(get(), get()) }
    single { GalleryPhotoRepository(androidApplication()) }

    // DataBase, Dao
    single { provideDB(androidApplication()) }
    single { provideLocationDao(get()) }
    single { provideRestaurantDao(get()) }
    single { provideFoodMenuBasketDao(get()) }


    // Retrofit
    single { provideGsonConvertFactory() }
    single { buildOkHttpClient() }

    single(named("map")) { provideMapRetrofit(get(), get()) }
    single { provideMapApiService(get(qualifier = named("map"))) }

    single(named("food")) { provideFoodRetrofit(get(), get()) }
    single { provideFoodApiService(get(qualifier = named("food"))) }

    // ViewModel
    viewModel {
        HomeVieModel(get(), get(), get()) }
    viewModel {
        MyViewModel(get(), get(), get()) }
    viewModel {
        RestaurantLikeListViewModel(get()) }
    viewModel { (restaurantCategory: RestaurantCategory, locationLatLng: LocationLatLngEntity) ->
        RestaurantListViewModel(restaurantCategory, locationLatLng, get())
    }
    viewModel { (mapSearchInfoEntity: MapSearchInfoEntity) ->
        MyLocationViewModel(mapSearchInfoEntity, get(), get())
    }
    viewModel { (restaurantEntity : RestaurantEntity) -> RestaurantDetailViewModel(restaurantEntity, get(),get()) }
    viewModel { (restaurantId : Long, foodEntityList : List<RestaurantFoodEntity>) ->
        RestaurantMenuListViewModel(restaurantId, foodEntityList, get())
    }
    viewModel { (restaurantTitle:String) -> RestaurantReviewListViewModel(restaurantTitle, get()) }
    viewModel { OrderMenuListViewModel(get(), get())  }
    viewModel { GalleryViewModel(get()) }

}