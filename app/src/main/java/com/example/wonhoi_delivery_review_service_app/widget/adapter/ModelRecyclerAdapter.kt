package com.example.wonhoi_delivery_review_service_app.widget.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.wonhoi_delivery_review_service_app.model.CellType
import com.example.wonhoi_delivery_review_service_app.model.Model
import com.example.wonhoi_delivery_review_service_app.screen.base.BaseViewModel
import com.example.wonhoi_delivery_review_service_app.util.mapper.ModelViewHolderMapper
import com.example.wonhoi_delivery_review_service_app.util.provider.ResourcesProvider
import com.example.wonhoi_delivery_review_service_app.widget.adapter.listener.AdapterListener
import com.example.wonhoi_delivery_review_service_app.widget.adapter.viewholder.ModelViewHolder

class ModelRecyclerAdapter<M : Model, VM : BaseViewModel>(
    private var modelList : List<Model>,
    private val viewModel : VM,
    private val resourcesProvider: ResourcesProvider,
    private val adapterListener : AdapterListener
) : ListAdapter<Model, ModelViewHolder<M>>(Model.DIFF_CALLBACK) {

    override fun getItemCount(): Int  = modelList.size

    override fun getItemViewType(position: Int): Int = modelList[position].type.ordinal // CellType 인덱스로

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ModelViewHolder<M> {
        return ModelViewHolderMapper.map(parent, CellType.values()[viewType], viewModel, resourcesProvider )
    }

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(holder: ModelViewHolder<M>, position: Int) {
        with(holder) {
            bindData(modelList[position] as M)
            bindViews(modelList[position] as M, adapterListener)
        }
    }

    override fun submitList(list: List<Model>?) {
        list?.let {
            modelList = it
        }
        super.submitList(list)
    }

}