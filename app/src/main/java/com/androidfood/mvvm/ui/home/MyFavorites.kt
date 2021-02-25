package com.androidfood.mvvm.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidfood.mvvm.R
import com.androidfood.mvvm.adapter.FavoritesItemsAdapter
import com.androidfood.mvvm.base.BaseActivity
import com.androidfood.mvvm.callback.ActionCallback
import com.androidfood.mvvm.data.model.RestaurantItem
import com.androidfood.mvvm.data.model.auth.Register.Restaurant.Restaurant
import com.androidfood.mvvm.data.remote.GeneralApiResponse
import com.androidfood.mvvm.databinding.ActivityMyFavoritesBinding
import com.androidfood.mvvm.helper.GeneralHelper
import com.androidfood.mvvm.ui.restaurant.RestaurantActivity
import com.androidfood.mvvm.vm.restaurant.UserRestaurantViewModel
import kotlinx.android.synthetic.main.activity_my_favorites.*

class MyFavorites : BaseActivity(), ActionCallback {

    val userRestaurantViewModel: UserRestaurantViewModel by viewModels()
    private val binding: ActivityMyFavoritesBinding by binding(R.layout.activity_my_favorites)
    private var restaurantListResponse = listOf<Restaurant>()
    private var restaurantList: MutableList<RestaurantItem> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.aboutToolbar.titleTbTxt.text = "My Favorites"
        binding.aboutToolbar.callback = this

        userRestaurantViewModel.getUserFavRestaurantList(true)
        userRestaurantViewModel.userResutaurantState.observe(this, Observer {
            when (it) {
                is GeneralApiResponse.Loading -> {
                    binding.loadingView.visibility = View.VISIBLE
                }

                is GeneralApiResponse.Failure -> {
                    binding.loadingView.visibility = View.GONE
                    GeneralHelper.showDialog(this, it.error, object : ActionCallback {
                        override fun onBindingClick(view: View) {
                        }

                        override fun onBindingClick(id: Int) {
                        }

                    })
                }

                is GeneralApiResponse.Success -> {
                    binding.loadingView.visibility = View.GONE
                    if (it.data?.data?.restaurants!!.size > 0) {
                        restaurantListResponse = it.data?.data?.restaurants!!
                        restaurantList.clear()
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
                        RV_fav.layoutManager =
                            GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false)

                        val favoritesItemsAdapter = FavoritesItemsAdapter(restaurantList, itemClick)
                        RV_fav.adapter = favoritesItemsAdapter
                    } else {
                        TV_noRestaurant.visibility = View.VISIBLE
                    }

                }
            }
        })
    }

    private val itemClick = object : FavoritesItemsAdapter.OnItemClickListener {
        override fun onItemClick(item: RestaurantItem, position: Int) {
            var bundle: Bundle = Bundle()
            bundle.putSerializable("RESTAURANT", restaurantListResponse.get(position))
            val intent = Intent(this@MyFavorites, RestaurantActivity::class.java)
            intent.putExtras(bundle!!)
            startActivity(intent)
            //startActivity<RestaurantActivity>("RESTAURANT" to intent)

        }

    }

    override fun onBindingClick(view: View) {
        when (view.id) {
            R.id.title_tb_arrow -> {
                finish()
            }
        }
    }

    override fun onBindingClick(id: Int) {
    }
}