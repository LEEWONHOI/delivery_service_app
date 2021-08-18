package com.example.wonhoi_delivery_review_service_app.screen.main.home.restaurant.detail

import android.app.AlertDialog
import android.content.ClipDescription.MIMETYPE_TEXT_PLAIN
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.example.wonhoi_delivery_review_service_app.R
import com.example.wonhoi_delivery_review_service_app.data.entity.RestaurantEntity
import com.example.wonhoi_delivery_review_service_app.data.entity.RestaurantFoodEntity
import com.example.wonhoi_delivery_review_service_app.databinding.ActivityRestaurantDetailBinding
import com.example.wonhoi_delivery_review_service_app.extensions.fromDpToPx
import com.example.wonhoi_delivery_review_service_app.extensions.load
import com.example.wonhoi_delivery_review_service_app.screen.base.BaseActivity
import com.example.wonhoi_delivery_review_service_app.screen.main.MainTabMenu
import com.example.wonhoi_delivery_review_service_app.screen.main.home.restaurant.RestaurantListFragment
import com.example.wonhoi_delivery_review_service_app.screen.main.home.restaurant.detail.menu.RestaurantMenuListFragment
import com.example.wonhoi_delivery_review_service_app.screen.main.home.restaurant.detail.review.RestaurantReviewListFragment
import com.example.wonhoi_delivery_review_service_app.screen.order.OrderMenuListActivity
import com.example.wonhoi_delivery_review_service_app.util.event.MenuChangeEventBus
import com.example.wonhoi_delivery_review_service_app.widget.adapter.RestaurantDetailListFragmentPageAdapter
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import kotlin.math.abs

class RestaurantDetailActivity :
    BaseActivity<RestaurantDetailViewModel, ActivityRestaurantDetailBinding>() {

    companion object {
        fun newIntent(context: Context, restaurantEntity: RestaurantEntity) =
            Intent(context, RestaurantDetailActivity::class.java).apply {
                putExtra(RestaurantListFragment.RESTAURANT_KEY, restaurantEntity)
            }
    }

    override fun getViewBinding(): ActivityRestaurantDetailBinding =
        ActivityRestaurantDetailBinding.inflate(layoutInflater)

    override val viewModel by viewModel<RestaurantDetailViewModel> {
        parametersOf(   // RestaurantListFragment 에서 RESTAURANT_KEY 로 보낸 intent 캐치 해서 viewModel 로 전달
            intent.getParcelableExtra<RestaurantEntity>(RestaurantListFragment.RESTAURANT_KEY)
        )
    }

    private val menuChangeEventBus by inject<MenuChangeEventBus>()

    override fun initViews() {
        initAppBar()
    }

    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }

    private lateinit var viewPageAdapter: RestaurantDetailListFragmentPageAdapter

    private fun initAppBar() = with(binding) {
        appBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            val topPadding = 300f.fromDpToPx().toFloat()
            val realAlphaScrollHeight = appBarLayout.measuredHeight - appBarLayout.totalScrollRange
            val abstractOffset = abs(verticalOffset)

            val realAlphaVerticalOffset: Float =
                if (abstractOffset - topPadding < 0) 0f
                else abstractOffset - topPadding

            // 스크롤이 topPadding 만큼 되지 않았으면 app bar 는 보이면 안됨
            if (abstractOffset < topPadding) {
                restaurantTitleTextView.alpha = 0f
                return@OnOffsetChangedListener
            }

            val percentage = realAlphaVerticalOffset / realAlphaScrollHeight
            restaurantTitleTextView.alpha =
                1 - (if (1 - percentage * 2 < 0) 0f else 1 - percentage * 2)
        })
        toolbar.setNavigationOnClickListener { finish() }

        // 전화 기능
        callButton.setOnClickListener {
            viewModel.getRestaurantTelNumber()?.let { telNumber ->
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel : $telNumber"))
                startActivity(intent)
            }
        }
        // 좋아요 기능
        likeButton.setOnClickListener {
            viewModel.toggleLikedRestaurant()
        }
        // 공유 기능
        shareButton.setOnClickListener {
            viewModel.getRestaurantInfo()?.let { restaurantInfo ->
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = MIMETYPE_TEXT_PLAIN  // 전달할 타입
                    putExtra( // 보낼 데이터
                        Intent.EXTRA_TEXT,
                        "맛있는 음식점 : ${restaurantInfo.restaurantTitle}" +
                                "\n평점 : ${restaurantInfo.grade}" +
                                "\n연락처 : ${restaurantInfo.restaurantTelNumber}"
                    )
                    Intent.createChooser(this, "친구에게 공유하기") // Android Sharesheet 를 사용해서 공유하기 기능 사용
                }
                startActivity(intent)
            }
        }
    }

    override fun observeDate() =
        viewModel.restaurantDetailStateLiveData.observe(this) { restaurantDetailState ->
            when (restaurantDetailState) {
                is RestaurantDetailState.Loading -> {
                    handleLoading()
                }

                is RestaurantDetailState.Success -> {
                    handleSuccess(restaurantDetailState)
                }
                else -> Unit
            }
        }

    private fun handleLoading() = with(binding) {
        progressBar.isVisible = true
    }

    private fun handleSuccess(state: RestaurantDetailState.Success) = with(binding) {
        progressBar.isGone = true

        val restaurantEntity = state.restaurantEntity

        callButton.isGone = restaurantEntity.restaurantTelNumber == null

        restaurantTitleTextView.text = restaurantEntity.restaurantTitle
        restaurantImage.load(restaurantEntity.restaurantImageUrl)
        restaurantMainTitleTextView.text = restaurantEntity.restaurantTitle

        ratingBar.rating = restaurantEntity.grade
        deliveryTimeText.text =
            getString(
                R.string.delivery_expected_time,
                restaurantEntity.deliveryTimeRange.first,
                restaurantEntity.deliveryTimeRange.second
            )
        deliveryTipText.text =
            getString(
                R.string.delivery_tip_range,
                restaurantEntity.deliveryTipRange.first,
                restaurantEntity.deliveryTipRange.second
            )

        likeText.setCompoundDrawablesWithIntrinsicBounds(
            ContextCompat.getDrawable(
                this@RestaurantDetailActivity,
                if (state.isLiked == true) {
                    R.drawable.ic_heart_enable
                } else {
                    R.drawable.ic_heart_disable
                }
            ), null, null, null
        )

        if (::viewPageAdapter.isInitialized.not()) {
            initViewPager(
                state.restaurantEntity.restaurantInfoId,
                state.restaurantEntity.restaurantTitle,
                state.restaurantFoodList
            )  // 레스토랑이름과 메뉴 정보 전달
        }

        notifyBasketCount(state.foodMenuListInBasket)

        // 장바구니 비우는 알람
        val (isClearNeed, afterAction) = state.isClearNeedInBasketAndAction
        if (isClearNeed) {
            alertClearNeedInBasket(afterAction)
        }

    }

    private fun initViewPager(
        restaurantInfoId: Long,
        restaurantTitle: String,
        restaurantFoodList: List<RestaurantFoodEntity>?
    ) {
        viewPageAdapter = RestaurantDetailListFragmentPageAdapter(
            this,
            listOf(
                // 메뉴
                RestaurantMenuListFragment.newInstance(
                    restaurantInfoId,
                    ArrayList(restaurantFoodList ?: listOf())
                ),
                // 리뷰
                RestaurantReviewListFragment.newInstance(
                    restaurantTitle
                )
            )
        )
        binding.menuAndReviewViewPager.adapter = viewPageAdapter    // 어뎁터 연결
        TabLayoutMediator(
            binding.menuAndReviewTabLayout,
            binding.menuAndReviewViewPager
        ) { tab, position ->
            tab.setText(RestaurantCategoryDetail.values()[position].categoryNameId)
        }.attach()
    }

    private fun notifyBasketCount(foodMenuListInBasket: List<RestaurantFoodEntity>?) =
        with(binding) {
            basketCountTextView.text = if (foodMenuListInBasket.isNullOrEmpty()) {
                "0"
            } else {
                getString(R.string.basket_count, foodMenuListInBasket.size)
            }
            basketButton.setOnClickListener {
                // 로그인 체크
                if (firebaseAuth.currentUser == null) {
                    alertLoginNeed {
                        lifecycleScope.launch {
                            menuChangeEventBus.changeMenu(MainTabMenu.MY)
                            finish()
                        }
                    }
                } else {    // 로그인 완료 -> 장바구니로 이동
                    startActivity(
                        OrderMenuListActivity.newIntent(this@RestaurantDetailActivity)
                    )
                }
            }
        }

    private fun alertLoginNeed(afterAction: () -> Unit) {
        AlertDialog.Builder(this)
            .setTitle("Login is required.")
            .setMessage("Login is required to order. Are you sure you want to go to the My tab?")
            .setPositiveButton("move") { dialog, _ ->
                afterAction()
                dialog.dismiss()
            }
            .setNegativeButton("cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun alertClearNeedInBasket(afterAction: () -> Unit) {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.in_basket_error))
            .setMessage(getString(R.string.in_basket_clear))
            .setPositiveButton(getString(R.string.put_in)) { dialog, _ ->
                viewModel.notifyClearBasket()
                afterAction()
                dialog.dismiss()
            }
            .setNegativeButton(getString(R.string.cancel)) {dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }


}