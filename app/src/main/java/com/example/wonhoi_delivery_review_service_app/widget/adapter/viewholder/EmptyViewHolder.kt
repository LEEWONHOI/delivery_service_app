package com.example.wonhoi_delivery_review_service_app.widget.adapter.viewholder

import com.example.wonhoi_delivery_review_service_app.databinding.ViewholderEmptyBinding
import com.example.wonhoi_delivery_review_service_app.model.Model
import com.example.wonhoi_delivery_review_service_app.screen.base.BaseViewModel
import com.example.wonhoi_delivery_review_service_app.util.provider.ResourcesProvider
import com.example.wonhoi_delivery_review_service_app.widget.adapter.listener.AdapterListener

class EmptyViewHolder(
    private val binding : ViewholderEmptyBinding,
    viewModel: BaseViewModel,
    resourcesProvider: ResourcesProvider
) : ModelViewHolder<Model>(binding, viewModel, resourcesProvider) {

    override fun reset()  = Unit

    override fun bindViews(model: Model, adapterListener: AdapterListener) = Unit
}