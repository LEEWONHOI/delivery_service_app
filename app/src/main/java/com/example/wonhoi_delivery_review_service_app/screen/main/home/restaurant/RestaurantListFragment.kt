package com.example.wonhoi_delivery_review_service_app.screen.main.home.restaurant

import android.util.Log
import android.widget.Toast
import androidx.core.os.bundleOf
import com.example.wonhoi_delivery_review_service_app.data.entity.LocationLatLngEntity
import com.example.wonhoi_delivery_review_service_app.databinding.FragmentRestaurantListBinding
import com.example.wonhoi_delivery_review_service_app.model.restaurant.RestaurantModel
import com.example.wonhoi_delivery_review_service_app.screen.base.BaseFragment
import com.example.wonhoi_delivery_review_service_app.screen.main.home.restaurant.detail.RestaurantDetailActivity
import com.example.wonhoi_delivery_review_service_app.util.provider.ResourcesProvider
import com.example.wonhoi_delivery_review_service_app.widget.adapter.ModelRecyclerAdapter
import com.example.wonhoi_delivery_review_service_app.widget.adapter.listener.restaurant.RestaurantListListener
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

// 검색된 레스토랑의 결과를 뿌려줄 프레그먼트
class RestaurantListFragment :
    BaseFragment<RestaurantListViewModel, FragmentRestaurantListBinding>() {

    private val restaurantCategory by lazy {    // getSerializable 지정된 키 반환 or null
        arguments?.getSerializable(RESTAURANT_CATEGORY_KEY) as RestaurantCategory
    }

    private val locationLatLng by lazy {
        arguments?.getParcelable<LocationLatLngEntity>(LOCATION_KEY)
    }

    override val viewModel by viewModel<RestaurantListViewModel> {
        parametersOf(
            restaurantCategory,
            locationLatLng
        )
    }

    override fun getViewBinding(): FragmentRestaurantListBinding = FragmentRestaurantListBinding.inflate(layoutInflater)

    private val resourcesProvider by inject<ResourcesProvider>()

    private val adapter by lazy {
        ModelRecyclerAdapter<RestaurantModel, RestaurantListViewModel>(
            listOf(),
            viewModel,
            resourcesProvider,
            adapterListener = object : RestaurantListListener{
            override fun onClickItem(model: RestaurantModel) {
               // Toast.makeText(requireContext(), "$model" ,Toast.LENGTH_SHORT).show()
                startActivity(
                    RestaurantDetailActivity.newIntent(
                        requireContext(),
                        model.toEntity()
                    )
                )
            }
        })
    }

    override fun initViews() = with(binding) {
        recyclerView.adapter = adapter
    }

    override fun observeData() = viewModel.restaurantListLiveData.observe(viewLifecycleOwner){
        Log.e("restaurantList", it.toString())
        adapter.submitList(it)
    }

    companion object {
        const val RESTAURANT_CATEGORY_KEY = "restaurantCategory"
        const val LOCATION_KEY = "location"
        const val RESTAURANT_KEY= "Restaurant"

        fun newInstance(
            restaurantCategory: RestaurantCategory,  locationLatLng: LocationLatLngEntity
        ) : RestaurantListFragment {
           return RestaurantListFragment().apply {
                arguments = bundleOf(
                    RESTAURANT_CATEGORY_KEY to restaurantCategory,
                    LOCATION_KEY to locationLatLng
                )
            }
        }
    }
}