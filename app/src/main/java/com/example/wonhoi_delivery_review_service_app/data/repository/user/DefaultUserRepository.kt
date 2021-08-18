package com.example.wonhoi_delivery_review_service_app.data.repository.user

import com.example.wonhoi_delivery_review_service_app.data.db.dao.LocationDao
import com.example.wonhoi_delivery_review_service_app.data.db.dao.RestaurantDao
import com.example.wonhoi_delivery_review_service_app.data.entity.LocationLatLngEntity
import com.example.wonhoi_delivery_review_service_app.data.entity.RestaurantEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DefaultUserRepository(
    private val locationDao: LocationDao,
    private val restaurantDao: RestaurantDao,
    private val ioDispatcher: CoroutineDispatcher
) : UserRepository {

    override suspend fun getUserLocation(): LocationLatLngEntity? = withContext(ioDispatcher) {
        locationDao.get(-1)
    }

    override suspend fun insertUserLocation(locationLatLngEntity: LocationLatLngEntity) = withContext(ioDispatcher) {
        locationDao.insert(locationLatLngEntity)
    }

    override suspend fun getUserLikedRestaurant(restaurantTitle: String): RestaurantEntity? = withContext(ioDispatcher){
        restaurantDao.get(restaurantTitle)
    }

    override suspend fun getAllUserLikeRestaurantList(): List<RestaurantEntity> = withContext(ioDispatcher) {
        restaurantDao.getAll()
    }

    override suspend fun insertUserLikeRestaurant(restaurantEntity: RestaurantEntity) = withContext(ioDispatcher){
        restaurantDao.insert(restaurantEntity)
    }

    override suspend fun deleteUserLikedRestaurant(restaurantTitle: String) = withContext(ioDispatcher){
        restaurantDao.delete(restaurantTitle)
    }

    override suspend fun deleteAllUserLikedRestaurant() = withContext(ioDispatcher){
        restaurantDao.deleteAll()
    }
}