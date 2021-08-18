package com.example.wonhoi_delivery_review_service_app.screen.base

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {

    protected var stateBundle : Bundle? = null

    open fun fetchData() : Job = viewModelScope.launch {  }

    open fun storeState(stateBundle : Bundle ) {    // 엑티비티, 프레그먼트 종료 전까지 데이터 유지
        this.stateBundle = stateBundle
    }

}