package com.example.wonhoi_delivery_review_service_app.screen.main.like

import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.example.wonhoi_delivery_review_service_app.databinding.FragmentRestaurantLikeListBinding
import com.example.wonhoi_delivery_review_service_app.model.restaurant.RestaurantModel
import com.example.wonhoi_delivery_review_service_app.screen.base.BaseFragment
import com.example.wonhoi_delivery_review_service_app.screen.main.home.restaurant.detail.RestaurantDetailActivity
import com.example.wonhoi_delivery_review_service_app.util.provider.ResourcesProvider
import com.example.wonhoi_delivery_review_service_app.widget.adapter.ModelRecyclerAdapter
import com.example.wonhoi_delivery_review_service_app.widget.adapter.listener.restaurant.RestaurantLikeListListener
import org.koin.android.ext.android.inject

class RestaurantLikeListFragment :
    BaseFragment<RestaurantLikeListViewModel, FragmentRestaurantLikeListBinding>() {

    override val viewModel by inject<RestaurantLikeListViewModel>()

    override fun getViewBinding(): FragmentRestaurantLikeListBinding = FragmentRestaurantLikeListBinding.inflate(layoutInflater)

    private val resourcesProvider by inject<ResourcesProvider>()

    private val adapter by lazy {
        ModelRecyclerAdapter<RestaurantModel, RestaurantLikeListViewModel>(
            listOf(),
            viewModel,
            resourcesProvider,
            adapterListener = object : RestaurantLikeListListener {
                override fun onDisLikeItem(model: RestaurantModel) {
                    viewModel.dislikeRestaurant(model.toEntity())
                }

                override fun onClickItem(model: RestaurantModel) {
                    startActivity(
                        RestaurantDetailActivity.newIntent(requireContext(), model.toEntity())
                    )
                }
            })
    }

    override fun initViews() {
        binding.recyclerView.adapter = adapter
    }

    override fun observeData() = viewModel.restaurantListLiveData.observe(viewLifecycleOwner) {
        checkListEmpty(it)
    }

    private fun checkListEmpty(restaurantList: List<RestaurantModel>) {
        val isEmpty = restaurantList.isEmpty()
        binding.recyclerView.isGone = isEmpty
        binding.emptyResultTextView.isVisible = isEmpty
        if (isEmpty.not()) {
            adapter.submitList(restaurantList)
        }
    }

    companion object {

        fun newInstance() = RestaurantLikeListFragment()

        const val TAG = "RestaurantLikeListFragment"

    }

}