package com.example.wonhoi_delivery_review_service_app.widget.adapter.viewholder.restaurant

import com.example.wonhoi_delivery_review_service_app.R
import com.example.wonhoi_delivery_review_service_app.databinding.ViewholderLikeRetaurantBinding
import com.example.wonhoi_delivery_review_service_app.extensions.clear
import com.example.wonhoi_delivery_review_service_app.extensions.load
import com.example.wonhoi_delivery_review_service_app.model.restaurant.RestaurantModel
import com.example.wonhoi_delivery_review_service_app.screen.base.BaseViewModel
import com.example.wonhoi_delivery_review_service_app.util.provider.ResourcesProvider
import com.example.wonhoi_delivery_review_service_app.widget.adapter.listener.AdapterListener
import com.example.wonhoi_delivery_review_service_app.widget.adapter.listener.restaurant.RestaurantLikeListListener
import com.example.wonhoi_delivery_review_service_app.widget.adapter.viewholder.ModelViewHolder

class LikeRestaurantViewHolder(
    private val binding: ViewholderLikeRetaurantBinding,
    viewModel: BaseViewModel,
    resourcesProvider: ResourcesProvider
) : ModelViewHolder<RestaurantModel>(binding, viewModel, resourcesProvider) {

    override fun reset() = with(binding) {
        restaurantImage.clear()
    }

    override fun bindData(model: RestaurantModel) {
        super.bindData(model)
        with(binding) {
            restaurantImage.load(model.restaurantImageUrl, 24f)
            restaurantTitleText.text = model.restaurantTitle
            gradeText.text = resourcesProvider.getString(R.string.grade_format, model.grade )
            reviewCountText.text = resourcesProvider.getString(R.string.review_count, model.reviewCount)

            val (minTime, maxTime) = model.deliveryTimeRange
            deliveryTimeText.text = resourcesProvider.getString(R.string.delivery_time, minTime, maxTime)

            val (minTip, maxTip) = model.deliveryTipRange
            deliveryTipText.text = resourcesProvider.getString(R.string.delivery_tip, minTip, maxTip)
        }
    }

    override fun bindViews(model: RestaurantModel, adapterListener: AdapterListener) = with(binding) {
        if (adapterListener is RestaurantLikeListListener) {
            root.setOnClickListener {
                adapterListener.onClickItem(model)
            }
            likeImageButton.setOnClickListener {
                adapterListener.onDisLikeItem(model)
            }
        }
    }

}