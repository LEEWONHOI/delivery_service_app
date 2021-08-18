package com.example.wonhoi_delivery_review_service_app.screen.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.Job

abstract class BaseActivity<VM : BaseViewModel, VB : ViewBinding> : AppCompatActivity() {

    abstract val viewModel : VM

    protected lateinit var binding : VB // onCreate 이후 처리 예정

    private lateinit var fetchJob : Job

    abstract fun getViewBinding() : VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getViewBinding()
        setContentView(binding.root)
        initState()
    }

    // 상태 초기화
    open fun initState() {
        initViews()
        fetchJob = viewModel.fetchData()
        observeDate()
    }

    // 뷰 초기화
    open fun initViews() = Unit

    // 데이터 호출 및 변화가 이뤄진걸 구독해서 처리할 옵저브
    abstract fun observeDate()

    override fun onDestroy() {
        if (fetchJob.isActive) {
            fetchJob.cancel()
        }
        super.onDestroy()
    }


}