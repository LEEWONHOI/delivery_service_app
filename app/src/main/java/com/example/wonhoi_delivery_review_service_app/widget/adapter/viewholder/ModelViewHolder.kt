package com.example.wonhoi_delivery_review_service_app.widget.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.wonhoi_delivery_review_service_app.model.Model
import com.example.wonhoi_delivery_review_service_app.screen.base.BaseViewModel
import com.example.wonhoi_delivery_review_service_app.util.provider.ResourcesProvider
import com.example.wonhoi_delivery_review_service_app.widget.adapter.listener.AdapterListener

abstract class ModelViewHolder<M : Model>(
    binding: ViewBinding,
    protected val viewModel: BaseViewModel,
    protected val resourcesProvider: ResourcesProvider
) : RecyclerView.ViewHolder(binding.root) {

    // bind data 초기화용
    abstract fun reset()

    open fun bindData(model: M) {
        reset()
    }

    abstract fun bindViews(model: M, adapterListener: AdapterListener)


}