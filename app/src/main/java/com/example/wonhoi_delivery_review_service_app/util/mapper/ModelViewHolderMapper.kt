package com.example.wonhoi_delivery_review_service_app.util.mapper

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.wonhoi_delivery_review_service_app.databinding.*
import com.example.wonhoi_delivery_review_service_app.model.CellType
import com.example.wonhoi_delivery_review_service_app.model.Model
import com.example.wonhoi_delivery_review_service_app.screen.base.BaseViewModel
import com.example.wonhoi_delivery_review_service_app.util.provider.ResourcesProvider
import com.example.wonhoi_delivery_review_service_app.widget.adapter.viewholder.EmptyViewHolder
import com.example.wonhoi_delivery_review_service_app.widget.adapter.viewholder.ModelViewHolder
import com.example.wonhoi_delivery_review_service_app.widget.adapter.viewholder.food.FoodMenuViewHolder
import com.example.wonhoi_delivery_review_service_app.widget.adapter.viewholder.order.OrderMenuViewHolder
import com.example.wonhoi_delivery_review_service_app.widget.adapter.viewholder.order.OrderViewHolder
import com.example.wonhoi_delivery_review_service_app.widget.adapter.viewholder.restaurant.LikeRestaurantViewHolder
import com.example.wonhoi_delivery_review_service_app.widget.adapter.viewholder.restaurant.RestaurantViewHolder
import com.example.wonhoi_delivery_review_service_app.widget.adapter.viewholder.review.RestaurantReviewViewHolder

object ModelViewHolderMapper {

    @Suppress("UNCHECKED_CAST")
    fun <M : Model> map(  // viewBinding 을 viewHolder 에서 넘겨주는 형태로 구현
        parent: ViewGroup,
        type: CellType,
        viewModel: BaseViewModel,
        resourcesProvider: ResourcesProvider
    ): ModelViewHolder<M> {
        val inflater = LayoutInflater.from(parent.context)
        val viewHolder = when (type) {
            CellType.EMPTY_CELL -> EmptyViewHolder(
                ViewholderEmptyBinding.inflate(inflater, parent, false),
                viewModel,
                resourcesProvider
            )
            CellType.RESTAURANT_CELL -> RestaurantViewHolder(
                ViewholderRetaurantBinding.inflate(inflater, parent, false),
                viewModel,
                resourcesProvider
            )
            CellType.LIKE_RESTAURANT_CELL -> LikeRestaurantViewHolder(
                ViewholderLikeRetaurantBinding.inflate(inflater, parent, false),
                viewModel,
                resourcesProvider
            )
            CellType.FOOD_CELL -> FoodMenuViewHolder(
                ViewholderFoodMenuBinding.inflate(inflater, parent, false),
                viewModel,
                resourcesProvider
            )
            CellType.REVIEW_CELL -> RestaurantReviewViewHolder(
                ViewholderRestaurantReviewBinding.inflate(inflater, parent, false),
                viewModel,
                resourcesProvider
            )
            CellType.ORDER_FOOD_CELL -> OrderMenuViewHolder(
                ViewholderOrderMenuBinding.inflate(inflater, parent, false),
                viewModel,
                resourcesProvider
            )
            CellType.ORDER_CELL -> OrderViewHolder(
                ViewholderOrderBinding.inflate(inflater, parent, false),
                viewModel,
                resourcesProvider
            )
        }
        return viewHolder as ModelViewHolder<M>
    }
}