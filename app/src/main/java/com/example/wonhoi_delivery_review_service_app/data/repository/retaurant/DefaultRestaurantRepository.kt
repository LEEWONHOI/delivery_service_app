package com.example.wonhoi_delivery_review_service_app.data.repository.retaurant

import com.example.wonhoi_delivery_review_service_app.data.entity.LocationLatLngEntity
import com.example.wonhoi_delivery_review_service_app.data.entity.RestaurantEntity
import com.example.wonhoi_delivery_review_service_app.data.network.MapApiService
import com.example.wonhoi_delivery_review_service_app.data.repository.retaurant.RestaurantRepository
import com.example.wonhoi_delivery_review_service_app.screen.main.home.restaurant.RestaurantCategory
import com.example.wonhoi_delivery_review_service_app.util.provider.ResourcesProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DefaultRestaurantRepository(
    private val mapApiService: MapApiService,
    private val resourcesProvider: ResourcesProvider,
    private val ioDispatcher: CoroutineDispatcher
) : RestaurantRepository {

    override suspend fun getList(
        restaurantCategory: RestaurantCategory,
        locationLatLngEntity: LocationLatLngEntity
    ): List<RestaurantEntity> = withContext(ioDispatcher) {

        //  API 를 통해서 데이터 받아오기
        val response = mapApiService.getSearchLocationAround(
            categories = resourcesProvider.getString(restaurantCategory.categoryTypeId),
            centerLat = locationLatLngEntity.latitude.toString(),
            centerLon = locationLatLngEntity.longitude.toString(),
            searchType = "name",
            radius = "1",
            resCoordType = "EPSG3857",
            searchtypCd = "A",
            reqCoordType = "WGS84GEO"
        )

        if(response.isSuccessful) {
            response.body()?.searchPoiInfo?.pois?.poi?.map { poi ->
                RestaurantEntity(
                    id = hashCode().toLong(),
                    restaurantInfoId = (1..10).random().toLong(),
                    restaurantCategory = restaurantCategory,
                    restaurantTitle = poi.name ?: "제목 없음",
                    restaurantImageUrl = "https://picsum.photos/200",
                    grade = (1 until 5).random() + ((0..10).random() / 10f),
                    reviewCount = (0 until 200).random(),
                    deliveryTimeRange = Pair((0..20).random(), (40..60).random()),
                    deliveryTipRange = Pair((0..1000).random(), (2000..4000).random()),
                    restaurantTelNumber = poi.telNo
                    )
            } ?: listOf()
        } else {
            listOf()
        }

//        listOf(
//            RestaurantEntity(
//                id = 0,
//                restaurantInfoId = 100,
//                restaurantCategory = RestaurantCategory.ALL,
//                restaurantTitle = "마포화로집",
//                restaurantImageUrl = "https://picsum.photos/200",
//                grade = (1 until 5).random() + ((0..10).random() / 10f),
//                reviewCount = (0 until 200).random(),
//                deliveryTimeRange = Pair(0, 20),
//                deliveryTipRange = Pair(0, 2000)
//            ),
//            RestaurantEntity(
//                id = 1,
//                restaurantInfoId = 10,
//                restaurantCategory = RestaurantCategory.ALL,
//                restaurantTitle = "옛날우동&덮밥",
//                restaurantImageUrl = "https://picsum.photos/200",
//                grade = (1 until 5).random() + ((0..10).random() / 10f),
//                reviewCount = (0 until 200).random(),
//                deliveryTimeRange = Pair(0, 20),
//                deliveryTipRange = Pair(0, 2000)
//            ),
//            RestaurantEntity(
//                id = 2,
//                restaurantInfoId = 20,
//                restaurantCategory = RestaurantCategory.ALL,
//                restaurantTitle = "마스터석쇠불고기&냉면plus",
//                restaurantImageUrl = "https://picsum.photos/200",
//                grade = (1 until 5).random() + ((0..10).random() / 10f),
//                reviewCount = (0 until 200).random(),
//                deliveryTimeRange = Pair(0, 20),
//                deliveryTipRange = Pair(0, 2000)
//            ),
//            RestaurantEntity(
//                id = 3,
//                restaurantInfoId = 30,
//                restaurantCategory = RestaurantCategory.ALL,
//                restaurantTitle = "마스터통삼겹",
//                restaurantImageUrl = "https://picsum.photos/200",
//                grade = (1 until 5).random() + ((0..10).random() / 10f),
//                reviewCount = (0 until 200).random(),
//                deliveryTimeRange = Pair(0, 20),
//                deliveryTipRange = Pair(0, 2000)
//            ),
//            RestaurantEntity(
//                id = 4,
//                restaurantInfoId = 40,
//                restaurantCategory = RestaurantCategory.ALL,
//                restaurantTitle = "창영이 족발&보쌈",
//                restaurantImageUrl = "https://picsum.photos/200",
//                grade = (1 until 5).random() + ((0..10).random() / 10f),
//                reviewCount = (0 until 200).random(),
//                deliveryTimeRange = Pair(0, 20),
//                deliveryTipRange = Pair(0, 2000)
//            ),
//            RestaurantEntity(
//                id = 5,
//                restaurantInfoId = 50,
//                restaurantCategory = RestaurantCategory.ALL,
//                restaurantTitle = "콩나물국밥&코다리조림 콩심 인천논현점",
//                restaurantImageUrl = "https://picsum.photos/200",
//                grade = (1 until 5).random() + ((0..10).random() / 10f),
//                reviewCount = (0 until 200).random(),
//                deliveryTimeRange = Pair(0, 20),
//                deliveryTipRange = Pair(0, 2000)
//            ),
//            RestaurantEntity(
//                id = 6,
//                restaurantInfoId = 60,
//                restaurantCategory = RestaurantCategory.ALL,
//                restaurantTitle = "김여사 칼국수&냉면 논현점",
//                restaurantImageUrl = "https://picsum.photos/200",
//                grade = (1 until 5).random() + ((0..10).random() / 10f),
//                reviewCount = (0 until 200).random(),
//                deliveryTimeRange = Pair(0, 20),
//                deliveryTipRange = Pair(0, 2000)
//            ),
//            RestaurantEntity(
//                id = 7,
//                restaurantInfoId = 70,
//                restaurantCategory = RestaurantCategory.ALL,
//                restaurantTitle = "돈키호테",
//                restaurantImageUrl = "https://picsum.photos/200",
//                grade = (1 until 5).random() + ((0..10).random() / 10f),
//                reviewCount = (0 until 200).random(),
//                deliveryTimeRange = Pair(0, 20),
//                deliveryTipRange = Pair(0, 2000)
//            ),
//        )
    }
}
//    RestaurantCategory.KOREAN_FOOD -> {
//        listOf(
//            RestaurantEntity(
//                id = 0,
//                restaurantCategory = RestaurantCategory.KOREAN_FOOD,
//                restaurantTitle = "마포화로집",
//                restaurantImageUrl = "https://picsum.photos/200",
//                grade = (1 until 5).random() + ((0..10).random() / 10f),
//                reviewCount = (0 until 200).random(),
//                deliveryTimeRange = Pair(0, 20),
//                deliveryTipRange = Pair(0, 2000)
//            ),
//            RestaurantEntity(
//                id = 1,
//                restaurantCategory = RestaurantCategory.KOREAN_FOOD,
//                restaurantTitle = "옛날우동&덮밥",
//                restaurantImageUrl = "https://picsum.photos/200",
//                grade = (1 until 5).random() + ((0..10).random() / 10f),
//                reviewCount = (0 until 200).random(),
//                deliveryTimeRange = Pair(0, 20),
//                deliveryTipRange = Pair(0, 2000)
//            ),
//            RestaurantEntity(
//                id = 2,
//                restaurantCategory = RestaurantCategory.KOREAN_FOOD,
//                restaurantTitle = "마스터석쇠불고기&냉면plus",
//                restaurantImageUrl = "https://picsum.photos/200",
//                grade = (1 until 5).random() + ((0..10).random() / 10f),
//                reviewCount = (0 until 200).random(),
//                deliveryTimeRange = Pair(0, 20),
//                deliveryTipRange = Pair(0, 2000)
//            ),
//            RestaurantEntity(
//                id = 3,
//                restaurantCategory = RestaurantCategory.KOREAN_FOOD,
//                restaurantTitle = "마스터통삼겹",
//                restaurantImageUrl = "https://picsum.photos/200",
//                grade = (1 until 5).random() + ((0..10).random() / 10f),
//                reviewCount = (0 until 200).random(),
//                deliveryTimeRange = Pair(0, 20),
//                deliveryTipRange = Pair(0, 2000)
//            ),
//            RestaurantEntity(
//                id = 4,
//                restaurantCategory = RestaurantCategory.KOREAN_FOOD,
//                restaurantTitle = "창영이 족발&보쌈",
//                restaurantImageUrl = "https://picsum.photos/200",
//                grade = (1 until 5).random() + ((0..10).random() / 10f),
//                reviewCount = (0 until 200).random(),
//                deliveryTimeRange = Pair(0, 20),
//                deliveryTipRange = Pair(0, 2000)
//            ),
//            RestaurantEntity(
//                id = 5,
//                restaurantCategory = RestaurantCategory.KOREAN_FOOD,
//                restaurantTitle = "콩나물국밥&코다리조림 콩심 인천논현점",
//                restaurantImageUrl = "https://picsum.photos/200",
//                grade = (1 until 5).random() + ((0..10).random() / 10f),
//                reviewCount = (0 until 200).random(),
//                deliveryTimeRange = Pair(0, 20),
//                deliveryTipRange = Pair(0, 2000)
//            ),
//        )
//    }
//    else -> {
//        listOf()
//    }

