package com.androidfood.mvvm.ui.restaurant

import android.Manifest
import android.app.ActionBar
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.RatingBar
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.livinglifetechway.quickpermissions_kotlin.runWithPermissions
import com.androidfood.mvvm.R
import com.androidfood.mvvm.adapter.DetailFoodItemsAdapter
import com.androidfood.mvvm.adapter.ReviewItemAdapter
import com.androidfood.mvvm.base.BaseActivity
import com.androidfood.mvvm.callback.ActionCallback
import com.androidfood.mvvm.callback.ItemPositionCallback
import com.androidfood.mvvm.constants.AppConstants_Java
import com.androidfood.mvvm.data.model.auth.Register.Restaurant.Restaurant
import com.androidfood.mvvm.data.model.menus.GetMenus
import com.androidfood.mvvm.data.model.menus.RestaurantMenu
import com.androidfood.mvvm.data.model.reviews.AddReview
import com.androidfood.mvvm.data.model.reviews.GetReviews
import com.androidfood.mvvm.data.model.reviews.ReviewData
import com.androidfood.mvvm.data.remote.GeneralApiResponse
import com.androidfood.mvvm.helper.GeneralHelper
import com.androidfood.mvvm.vm.favorites.FavViewModel
import com.androidfood.mvvm.vm.restaurant_menu.RestaurantMenuViewModel
import com.androidfood.mvvm.vm.reviews.ReviewsViewModel
import kotlinx.android.synthetic.main.activity_restaurant.*
import org.jetbrains.anko.find
import java.util.*
import kotlin.collections.ArrayList


class RestaurantActivity : BaseActivity(), OnMapReadyCallback, ItemPositionCallback {

    private var MenuList: MutableList<GetMenus> = ArrayList()
    private val reviewsViewModel: ReviewsViewModel by viewModels()
    private val restaurantMenuViewModel: RestaurantMenuViewModel by viewModels()

    private var restaurantList = listOf<ReviewData>()

    private lateinit var mMap: GoogleMap

    val favViewModel: FavViewModel by viewModels()
    private lateinit var resData: Restaurant

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant)
        val bar: ActionBar? = actionBar
        bar?.setBackgroundDrawable(
            ColorDrawable(
                ContextCompat.getColor(
                    this,
                    R.color.colorPrimaryDark
                )
            )
        )
        val mapFragment: SupportMapFragment? =
            supportFragmentManager.findFragmentById(R.id.restaurant_address_map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
        restaurant_back.setOnClickListener {
            onBackPressed()
//            finish()
        }


        restaurant_review_add_label.setOnClickListener {
            showDialog(false, null)
        }

        reviewsViewModel.AddReviewState.observe(this, Observer {
            when (it) {

                is GeneralApiResponse.Loading -> {
                    loadingView.visibility = View.VISIBLE
                }

                is GeneralApiResponse.Failure -> {
                    loadingView.visibility = View.GONE
                }

                is GeneralApiResponse.Success<AddReview> -> {
                    loadingView.visibility = View.GONE
                    reviewsViewModel.getReviews()
                }
            }
        })
        reviewsViewModel.GetReviewState.observe(this, Observer {
            when (it) {

                is GeneralApiResponse.Loading -> {
                    loadingView.visibility = View.VISIBLE
                }

                is GeneralApiResponse.Failure -> {
                    loadingView.visibility = View.GONE
                }

                is GeneralApiResponse.Success<GetReviews> -> {
                    loadingView.visibility = View.GONE
                    restaurantList = it.data!!.reviewData
                    initRecyclerReviews()
                    //restaurantMenuViewModel.getMenus(resData.id)
                    restaurantMenuViewModel.getMenus(resData.id)
                }

            }
        })
        reviewsViewModel.UpdateReviewState.observe(this, Observer {
            when (it) {

                is GeneralApiResponse.Loading -> {
                    loadingView.visibility = View.VISIBLE
                }

                is GeneralApiResponse.Failure -> {
                    loadingView.visibility = View.GONE
                }

                is GeneralApiResponse.Success<AddReview> -> {
                    loadingView.visibility = View.GONE
                    reviewsViewModel.getReviews()
                }

            }
        })
        reviewsViewModel.DeleteReviewState.observe(this, Observer {
            when (it) {

                is GeneralApiResponse.Loading -> {
                    loadingView.visibility = View.VISIBLE
                }

                is GeneralApiResponse.Failure -> {
                    loadingView.visibility = View.GONE
                    GeneralHelper.showDialog(this, it.error, object : ActionCallback {
                        override fun onBindingClick(view: View) {
                        }

                        override fun onBindingClick(id: Int) {
                        }

                    })
                }

                is GeneralApiResponse.Success<Objects> -> {
                    loadingView.visibility = View.GONE
                    reviewsViewModel.getReviews()
                }

            }
        })
        favViewModel.favRestaurantState.observe(this, Observer {
            when (it) {
                is GeneralApiResponse.Loading -> {
                    loadingView.visibility = View.VISIBLE
                }
                is GeneralApiResponse.Failure -> {
                    loadingView.visibility = View.GONE
                }
                is GeneralApiResponse.Success<Object> -> {
                    loadingView.visibility = View.GONE
                    val map = it.data as Map<String, Object>
                    val data = map
                    if (data.get("data")!!.equals(true)) {
                        restaurant_favorite.visibility = View.VISIBLE
                        restaurant_favorite_selected.visibility = View.GONE
                    } else {
                        restaurant_favorite.visibility = View.GONE
                        restaurant_favorite_selected.visibility = View.VISIBLE
                    }
                }
            }
        })

        restaurantMenuViewModel.getMenusState.observe(this, Observer {
            when (it) {
                is GeneralApiResponse.Loading -> {
                }
                is GeneralApiResponse.Failure -> {
                }
                is GeneralApiResponse.Success<RestaurantMenu> -> {
                    for (i in it.data!!.data) {
                        MenuList.add(
                            GetMenus(
                                i.id,
                                i.user_id,
                                i.name,
                                i.image,
                                i.description,
                                i.image_url
                            )
                        )
                    }
                    initRecyclerMenu()
                }
            }
        })


        reviewsViewModel.getReviews()
        setUpScreenDataAndAction()


    }

    private fun initRecyclerReviews() {
        restaurant_review_rv.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            isNestedScrollingEnabled = false
            adapter = ReviewItemAdapter(restaurantList, itemClick)
        }
    }

    private fun initRecyclerMenu() {
        RV_menu.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = DetailFoodItemsAdapter(MenuList, this@RestaurantActivity)
        }
    }

    private val itemClick = object : ReviewItemAdapter.OnItemClickListener {
        override fun onItemClick(item: ReviewData) {
            if (item.user.id.equals(GeneralHelper.getUsersData().data.user.id)) {
                showDialog(true, item)
            }
        }

    }

    override fun onMapReady(map: GoogleMap?) {
        mMap = map!!
        setUpMap()
    }

    private fun setUpMap() = runWithPermissions(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) {
        mMap.uiSettings.isZoomControlsEnabled = false
        mMap.uiSettings.isMyLocationButtonEnabled = true
        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return@runWithPermissions
        }
        mMap.isMyLocationEnabled = true
    }


    private fun showDialog(isUpdate: Boolean, item: ReviewData?) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.add_review)

        val submit = dialog.findViewById(R.id.submit) as Button
        val delete = dialog.findViewById(R.id.delete) as Button
        val ratingBar = dialog.findViewById(R.id.ratingBar) as RatingBar
        val txtReview = dialog.find(R.id.txtReview) as TextView


        if (isUpdate) {
            delete.visibility = View.VISIBLE
            submit.text = "Update"
            txtReview.text = item?.review
            ratingBar.rating = item?.rating!!.toFloat()
        } else {
            delete.visibility = View.GONE
            submit.text = "Submit"
        }

        submit.setOnClickListener {
            if (isUpdate) {

                if (ratingBar.rating > 0 && txtReview.text.toString().isNotEmpty()) {
                    reviewsViewModel.updateReview(
                        item?.id!!,
                        ratingBar.rating.toDouble(),
                        txtReview.text.toString().trim()
                    )
                }

            } else {
                if (ratingBar.rating > 0 && txtReview.text.toString().isNotEmpty()) {
                    reviewsViewModel.addReview(
                        resData.id,
                        ratingBar.rating.toDouble(),
                        txtReview.text.toString().trim()
                    )
                }
            }

            dialog.dismiss()
        }

        delete.setOnClickListener {
            reviewsViewModel.deleteReview(item?.id!!)
            dialog.dismiss()
        }




        dialog.show()
    }

    override fun ClickedItemPosition(position: Int) {

    }

    private fun setUpScreenDataAndAction() {

        val intent = this.intent
        val bundle = intent.extras

        resData = bundle!!.getSerializable("RESTAURANT") as Restaurant
        restaurant_header_title.text = resData.name
        restaurant_header_review.text = "${resData.total_reviews} Reviews"
        restaurant_header_wishlist.text = "${resData.total_favorites} Favorites"
        restaurant_title.text = resData.description
        restaurant_address.text = resData.address

        if (resData.category.name != null || !resData.category.equals("")) {
            restaurantCategory.setText(resData.category.name)
            restaurantCategory.visibility = View.VISIBLE
        }


        restaurant_address_container.setOnClickListener {
            val strUri =
                "http://maps.google.com/maps?q=loc:" + resData.latitude + "," + resData.longitude + " (" + "Label which you want" + ")"
            val intent =
                Intent(Intent.ACTION_VIEW, Uri.parse(strUri))

            intent.setClassName(
                "com.google.android.apps.maps",
                "com.google.android.maps.MapsActivity"
            )

            startActivity(intent)

        }

        Glide.with(this)  //2
            .load(resData.attachments[0].attachment_url) //3
            .centerCrop() //4
            .placeholder(R.drawable.app_logo) //5
            .error(R.drawable.bg_primary) //6
            .into(restaurant_header_img) //8

        restaurant_call.setOnClickListener {

            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:${resData.phone}")
            startActivity(intent)
        }

        restaurant_direction.setOnClickListener {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?saddr=${resData.latitude},${resData.longitude}&daddr=${AppConstants_Java.lat},${AppConstants_Java.lng}")
            )
            startActivity(intent)

        }

        restaurant_favorite.setOnClickListener {
            favViewModel.favRestaurant(resData.id)
        }

        restaurant_favorite_selected.setOnClickListener {
            favViewModel.favRestaurant(resData.id)
        }

        restaurant_web.setOnClickListener {
            var webpage = Uri.parse(resData.url)
            if (!resData.url.startsWith("http://") && !resData.url.startsWith("https://")) {
                webpage = Uri.parse("http://" + resData.url);
            }

            val intent = Intent(Intent.ACTION_VIEW, webpage)
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            }
        }

        if (resData.is_favorite == 1) {
            restaurant_favorite.visibility = View.GONE
            restaurant_favorite_selected.visibility = View.VISIBLE
        } else {
            restaurant_favorite.visibility = View.VISIBLE
            restaurant_favorite_selected.visibility = View.GONE
        }


    }


}