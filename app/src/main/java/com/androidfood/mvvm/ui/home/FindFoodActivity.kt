package com.androidfood.mvvm.ui.home

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.annotation.Nullable
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidfood.mvvm.R
import com.androidfood.mvvm.adapter.RestaurantItemAdapter
import com.androidfood.mvvm.base.BaseActivity
import com.androidfood.mvvm.callback.ActionCallback
import com.androidfood.mvvm.data.model.Restaurant.Users
import com.androidfood.mvvm.data.model.RestaurantItem
import com.androidfood.mvvm.data.model.auth.Register.Restaurant.Restaurant
import com.androidfood.mvvm.data.remote.GeneralApiResponse
import com.androidfood.mvvm.databinding.ActivityFindFoodBinding
import com.androidfood.mvvm.ui.restaurant.Adress_Activity
import com.androidfood.mvvm.ui.restaurant.RestaurantActivity
import com.androidfood.mvvm.vm.restaurant.UserRestaurantViewModel


class FindFoodActivity : BaseActivity(), ActionCallback {

    private val binding: ActivityFindFoodBinding by binding(R.layout.activity_find_food)
    private val userRestaurantVM: UserRestaurantViewModel by viewModels()
    var lat: Double = 0.0
    var lng: Double = 0.0

    private var restaurantList: MutableList<RestaurantItem> = arrayListOf()
    private var restaurantListResponse = listOf<Restaurant>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.aboutToolbar.titleTbTxt.text = "Find Food"
        binding.aboutToolbar.callback = this

        binding.restaurantLocation.setInputType(0x00000000);
        val editText = binding.restaurantLocation

        binding.restaurantName.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                //searchViewModel.insertInDB(query)

                if (binding.restaurantName.query.toString()
                        .isNotEmpty() && binding.restaurantLocation.query.toString().isNotEmpty()
                ) {
                    binding.RVFindFood.visibility = View.GONE
                    binding.TVNoRestaurant.visibility = View.GONE
                    binding.shimmerLayout.visibility = View.VISIBLE
                    binding.shimmerViewContainer.startShimmer()

                    Handler().postDelayed({
                        userRestaurantVM.getUserRestaurantList(
                            lat,
                            lng,
                            query
                        )
                    }, 3000)

                }

                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })

        binding.restaurantLocation.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                //searchViewModel.insertInDB(query)

                if (binding.restaurantName.query.toString()
                        .isNotEmpty() && binding.restaurantLocation.query.toString()
                        .isNotEmpty()
                ) {
                    binding.RVFindFood.visibility = View.GONE
                    binding.TVNoRestaurant.visibility = View.GONE
                    binding.shimmerLayout.visibility = View.VISIBLE
                    binding.shimmerViewContainer.startShimmer()

                    Handler().postDelayed({
                        userRestaurantVM.getUserRestaurantList(
                            lat,
                            lng,
                            binding.restaurantName.query.toString()
                        )
                    }, 3000)

                }

                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })

        binding.restaurantLocation.setIconifiedByDefault(false)
        binding.restaurantLocation.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, Adress_Activity::class.java)
            startActivityForResult(intent, 0);
        })
//        editText.setOnClickListener {
//            val intent = Intent(this, Adress_Activity::class.java)
//            startActivityForResult(intent, 0);
//        }

        userRestaurantVM.userResutaurantState.observe(this, Observer {
            when (it) {

                is GeneralApiResponse.Loading -> {
                    binding.shimmerViewContainer.startShimmer()
                }

                is GeneralApiResponse.Failure -> {
                    binding.shimmerLayout.visibility = View.GONE
                    binding.shimmerViewContainer.stopShimmer()
                }

                is GeneralApiResponse.Success<Users> -> {
                    if (it.data?.data!!.restaurants.size > 0) {
                        binding.shimmerViewContainer.stopShimmer()
                        binding.shimmerLayout.visibility = View.GONE
                        binding.TVNoRestaurant.visibility = View.GONE

                        restaurantList.clear()
                        restaurantListResponse = it.data?.data?.restaurants!!
                        for (i in it.data?.data!!.restaurants) {
                            restaurantList.add(
                                RestaurantItem(
                                    i.name,
                                    i.description,
                                    i.average_rating
                                    ,
                                    if (i.attachments.size > 0) i.attachments[0].attachment_url else ""
                                )
                            )
                        }
                        initRecycler()
                        binding.RVFindFood.visibility = View.VISIBLE
                    } else {
                        binding.shimmerLayout.visibility = View.GONE
                        binding.TVNoRestaurant.visibility = View.VISIBLE
                    }

                }

            }
        })

    }

    override fun onBindingClick(view: View) {
        when (view.id) {
            R.id.title_tb_arrow -> {
                finish()
            }

        }
    }

    override fun onBindingClick(id: Int) {
        TODO("Not yet implemented")
    }

    override
    fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        @Nullable data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0) {
            if (resultCode === RESULT_OK) {
                val add = data?.extras?.get("ADDRESS").toString()
                val loc = data?.extras?.get("LATLNG").toString()
                lat = data?.extras?.get("LAT") as Double
                lng = data?.extras?.get("LNG") as Double
                binding.restaurantLocation.setQuery(add, true)

            }
        }

    }

    private fun initRecycler() {
        binding.RVFindFood.apply {
            layoutManager = GridLayoutManager(context, 2, LinearLayoutManager.VERTICAL, false)
            isNestedScrollingEnabled = false
            adapter = RestaurantItemAdapter(restaurantList, itemClick)
        }
    }


    private val itemClick = object : RestaurantItemAdapter.OnItemClickListener {
        override fun onItemClick(item: RestaurantItem, position: Int) {
            var bundle: Bundle = Bundle()
            bundle.putSerializable("RESTAURANT", restaurantListResponse.get(position))
            val intent = Intent(this@FindFoodActivity, RestaurantActivity::class.java)
            intent.putExtras(bundle!!)
            startActivity(intent)
            //startActivity<RestaurantActivity>("RESTAURANT" to intent)

        }

    }

}