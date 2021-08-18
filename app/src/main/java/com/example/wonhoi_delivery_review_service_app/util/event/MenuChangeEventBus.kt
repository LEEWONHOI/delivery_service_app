package com.example.wonhoi_delivery_review_service_app.util.event

import com.example.wonhoi_delivery_review_service_app.screen.main.MainTabMenu
import kotlinx.coroutines.flow.MutableSharedFlow

class MenuChangeEventBus {

    val mainTabMenuFlow = MutableSharedFlow<MainTabMenu>()

    suspend fun changeMenu(menu : MainTabMenu) {
        mainTabMenuFlow.emit(menu)
    }

}