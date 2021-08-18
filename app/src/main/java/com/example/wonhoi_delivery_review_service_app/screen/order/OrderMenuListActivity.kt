package com.example.wonhoi_delivery_review_service_app.screen.order

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.example.wonhoi_delivery_review_service_app.databinding.ActivityOrderMenuListBinding
import com.example.wonhoi_delivery_review_service_app.model.restaurant.food.FoodModel
import com.example.wonhoi_delivery_review_service_app.screen.base.BaseActivity
import com.example.wonhoi_delivery_review_service_app.util.provider.ResourcesProvider
import com.example.wonhoi_delivery_review_service_app.widget.adapter.ModelRecyclerAdapter
import com.example.wonhoi_delivery_review_service_app.widget.adapter.listener.order.OrderMenuListListener
import org.koin.android.ext.android.inject

class OrderMenuListActivity : BaseActivity<OrderMenuListViewModel, ActivityOrderMenuListBinding>() {

    override val viewModel by inject<OrderMenuListViewModel>()

    override fun getViewBinding(): ActivityOrderMenuListBinding = ActivityOrderMenuListBinding.inflate(layoutInflater)

    private val resourcesProvider by inject<ResourcesProvider>()

    companion object {
        fun newIntent(context: Context) =
            Intent(
                context, OrderMenuListActivity::class.java
            )
    }

    private val adapter by lazy {
        ModelRecyclerAdapter<FoodModel, OrderMenuListViewModel>(
            listOf(),
            viewModel,
            resourcesProvider,
            adapterListener = object : OrderMenuListListener {
                override fun onRemoveItem(foodModel: FoodModel) {
                    viewModel.removeOrderMenu(foodModel)
                }
            })
    }

    override fun initViews() = with(binding) {
        recyclerView.adapter = adapter

        toolbar.setNavigationOnClickListener {
            finish()
        }

        confirmButton.setOnClickListener {
            viewModel.orderMenu()
        }

        orderClearButton.setOnClickListener {
            viewModel.clearOrderMenu()
        }
    }


    override fun observeDate() = viewModel.orderMenuStateLiveData.observe(this) { orderMenuState ->
        when(orderMenuState) {
            is OrderMenuState.Loading -> {
                handleLoading()
            }
            is OrderMenuState.Success -> {
                handleSuccess(orderMenuState)
            }
            is OrderMenuState.Order -> {
                handleOrderState()
            }
            is OrderMenuState.Error -> {
                handleErrorState(orderMenuState)
            }
            else -> Unit
        }
    }

    private fun handleLoading() = with(binding) {
        progressBar.isVisible = true

    }

    private fun handleSuccess(state: OrderMenuState.Success) = with(binding) {
        progressBar.isGone = true
        adapter.submitList(state.restaurantFoodModelList)

        val menuOrderIsEmpty = state.restaurantFoodModelList.isNullOrEmpty()
        confirmButton.isEnabled = menuOrderIsEmpty.not()

        if (menuOrderIsEmpty) {
            Toast.makeText(this@OrderMenuListActivity, "There is no food menu to order.", Toast.LENGTH_SHORT).show()
            finish()
        }

    }

    private fun handleOrderState()  {
        Toast.makeText(this, "Your order has been completed.", Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun handleErrorState(state: OrderMenuState.Error)  {
        Toast.makeText(this, getString(state.messageId, state.e), Toast.LENGTH_SHORT).show()
        finish()
    }

}