package com.example.wonhoi_delivery_review_service_app.screen.main.my

import android.app.Activity
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.example.wonhoi_delivery_review_service_app.R
import com.example.wonhoi_delivery_review_service_app.databinding.FragmentMyBinding
import com.example.wonhoi_delivery_review_service_app.extensions.load
import com.example.wonhoi_delivery_review_service_app.model.restaurant.order.OrderModel
import com.example.wonhoi_delivery_review_service_app.screen.base.BaseFragment
import com.example.wonhoi_delivery_review_service_app.screen.review.AddRestaurantReviewActivity
import com.example.wonhoi_delivery_review_service_app.util.provider.ResourcesProvider
import com.example.wonhoi_delivery_review_service_app.widget.adapter.ModelRecyclerAdapter
import com.example.wonhoi_delivery_review_service_app.widget.adapter.listener.AdapterListener
import com.example.wonhoi_delivery_review_service_app.widget.adapter.listener.order.OrderListListener
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class MyFragment : BaseFragment<MyViewModel, FragmentMyBinding>() {

    override val viewModel by viewModel<MyViewModel>()

    override fun getViewBinding(): FragmentMyBinding = FragmentMyBinding.inflate(layoutInflater)

    private val gso : GoogleSignInOptions by lazy {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
    }

    private val gsc by lazy {
        GoogleSignIn.getClient(requireActivity(), gso)
    }

    private val fireBaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private val loginLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
        if(activityResult.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(activityResult.data)
            try {
                task.getResult(ApiException::class.java)?.let { googleSignInAccount ->
                    viewModel.saveToken(googleSignInAccount.idToken ?: throw  Exception())
                }
            } catch (e : Exception) {
                e.printStackTrace()
            }
        }
    }

    private val resourcesProvider by inject<ResourcesProvider>()

    private val adapter by lazy {
        ModelRecyclerAdapter<OrderModel,MyViewModel>(
            listOf(),
            viewModel,
            resourcesProvider,
            adapterListener = object : OrderListListener {

                override fun writeRestaurantReview(orderId: String, restaurantTitle: String) {
                    startActivity(
                        AddRestaurantReviewActivity.newIntent(requireContext(), orderId, restaurantTitle)
                    )
                }
            })
    }

    override fun initViews() = with(binding) {
        loginButton.setOnClickListener {
            signInGoogle()
        }
        logoutButton.setOnClickListener {
            fireBaseAuth.signOut()
            viewModel.signOut()
        }
        recyclerView.adapter = adapter
    }

    private fun signInGoogle() {
        val signInIntent = gsc.signInIntent
        loginLauncher.launch(signInIntent)
    }

    override fun observeData() = viewModel.myStateLiveData.observe(viewLifecycleOwner) { myState ->
        when (myState) {
            is MyState.Loading -> {
                handleLoadingState()
            }
            is MyState.Success -> {
                handleSuccessState(myState)
            }
            is MyState.Login -> {
                handleLoginState(myState)
            }
            is MyState.Error -> {
                handleErrorState(myState)
            }
            else -> Unit
        }
    }

    private fun handleLoadingState() {
        binding.loginRequiredGroup.isGone = true
        binding.progressBar.isVisible = true

    }
    private fun handleSuccessState(myState : MyState.Success) = with(binding) {
        progressBar.isGone = true
        when(myState) {
            is MyState.Success.Registered -> {
                handleRegisteredState(myState)
            }
            is MyState.Success.NotRegistered -> {
                profileGroup.isGone = true
                loginRequiredGroup.isVisible = true
            }
        }
    }

    private fun handleRegisteredState(state : MyState.Success.Registered) = with(binding) {
        profileGroup.isVisible = true
        loginRequiredGroup.isGone = true
        profileImageView.load(state.profileImageUri.toString(), 60f)
        userNameTextView.text = state.userName

        // Toast.makeText(requireContext(), state.orderList.toString(), Toast.LENGTH_SHORT).show()
        adapter.submitList(state.orderList)

    }

    private fun handleLoginState(myState : MyState.Login) {
        binding.progressBar.isVisible = true
        val credential = GoogleAuthProvider.getCredential(myState.idToken, null)
        fireBaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) {
                if(it.isSuccessful) {
                    val user = fireBaseAuth.currentUser
                    viewModel.setUserInfo(user)
                } else {
                    fireBaseAuth.signOut()
                    viewModel.setUserInfo(null)
                }
            }
    }
    private fun handleErrorState(myState : MyState.Error) {

    }

    companion object {

        fun newInstance() = MyFragment()

        const val TAG = "MyFragment"

    }
}