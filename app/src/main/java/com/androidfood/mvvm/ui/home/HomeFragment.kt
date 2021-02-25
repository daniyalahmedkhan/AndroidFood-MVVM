package com.androidfood.mvvm.ui.home

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.androidfood.mvvm.R
import com.androidfood.mvvm.adapter.RestaurantItemAdapter
import com.androidfood.mvvm.base.BaseFragment
import com.androidfood.mvvm.constants.AppConstants_Java
import com.androidfood.mvvm.data.model.Restaurant.Users
import com.androidfood.mvvm.data.model.RestaurantItem
import com.androidfood.mvvm.data.model.auth.Register.Restaurant.Restaurant
import com.androidfood.mvvm.data.remote.GeneralApiResponse
import com.androidfood.mvvm.databinding.FragmentHomeBinding
import com.androidfood.mvvm.ui.restaurant.RestaurantActivity
import com.androidfood.mvvm.utils.GeneralResponse
import com.androidfood.mvvm.utils.LocationHelper
import com.androidfood.mvvm.vm.restaurant.UserRestaurantViewModel
import com.androidfood.mvvm.vm.home.home.SearchViewModel


/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */

class HomeFragment : BaseFragment() {

    private lateinit var binding: FragmentHomeBinding
    private val userRestaurantVM: UserRestaurantViewModel by viewModels()

    val searchViewModel: SearchViewModel by viewModels()

    private var restaurantList: MutableList<RestaurantItem> = arrayListOf()
    private var restaurantListResponse = listOf<Restaurant>()
    var manager: LocationManager? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            binding<FragmentHomeBinding>(
                inflater,
                R.layout.fragment_home,
                container
            ).apply {
                lifecycleOwner = viewLifecycleOwner
            }
        //   searchViewModel = ViewModelProvider(this).get(SearchViewModel::class.java)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activateHideKeyboardUponTouchingScreen(view)


        binding.homeFrgSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                //searchViewModel.insertInDB(query)

                binding.homeFrgRv.visibility = View.GONE
                binding.TVNoRestaurant.visibility = View.GONE
                binding.shimmerLayout.visibility = View.VISIBLE
                binding.shimmerViewContainer.startShimmer()

                Handler().postDelayed({
                    userRestaurantVM.getUserRestaurantList(
                        null,
                        null,
                        query
                    )
                }, 3000)

                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })


        binding.homeFrgFilter.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {

                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })

        binding.TVNoRestaurant.setOnClickListener {
            binding.homeFrgRv.visibility = View.GONE
            binding.TVNoRestaurant.visibility = View.GONE
            binding.shimmerLayout.visibility = View.VISIBLE
            val locationHelper = LocationHelper()
            locationHelper.LocationInitialize(context)

            Handler().postDelayed({
                userRestaurantVM.getUserRestaurantList(
                    AppConstants_Java.lat,
                    AppConstants_Java.lng,
                    ""
                )
            }, 3000)
        }

        searchViewModel.dbTranState.observe(viewLifecycleOwner, Observer {

            when (it) {
                is GeneralResponse.SuccessVal<Long> -> {
                    println(it)
                }
            }

        })
        userRestaurantVM.userResutaurantState.observe(viewLifecycleOwner, Observer {
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
                        binding.homeFrgRv.visibility = View.VISIBLE
                    } else {
                        binding.shimmerLayout.visibility = View.GONE
                        binding.TVNoRestaurant.visibility = View.VISIBLE
//                        GeneralHelper.showDialog(requireActivity(), "No Restaurant found nearby, Please try again!", object :
//                            ActionCallback {
//
//                            override fun onBindingClick(view: View) {
//                                binding.shimmerLayout.visibility = View.VISIBLE
//                                val locationHelper = LocationHelper()
//                                locationHelper.LocationInitialize(context)
//
//                                Handler().postDelayed({
//                                    userRestaurantVM.getUserRestaurantList(
//                                        AppConstants_Java.lat,
//                                        AppConstants_Java.lng,
//                                        ""
//                                    )
//                                }, 3000)
//                            }
//
//                            override fun onBindingClick(id: Int) {
//                                binding.shimmerLayout.visibility = View.VISIBLE
//                                val locationHelper = LocationHelper()
//                                locationHelper.LocationInitialize(context)
//
//                                Handler().postDelayed({
//                                    userRestaurantVM.getUserRestaurantList(
//                                        AppConstants_Java.lat,
//                                        AppConstants_Java.lng,
//                                        ""
//                                    )
//                                }, 3000)
//                            }
//
//                        })
                    }

                }

            }
        })
    }

    private fun initRecycler() {
        binding.homeFrgRv.apply {
            layoutManager = GridLayoutManager(context, 2, LinearLayoutManager.VERTICAL, false)
            isNestedScrollingEnabled = false
            adapter = RestaurantItemAdapter(restaurantList, itemClick)
        }
    }

    private val itemClick = object : RestaurantItemAdapter.OnItemClickListener {
        override fun onItemClick(item: RestaurantItem, position: Int) {
            var bundle: Bundle = Bundle()
            bundle.putSerializable("RESTAURANT", restaurantListResponse.get(position))
            val intent = Intent(requireActivity(), RestaurantActivity::class.java)
            intent.putExtras(bundle!!)
            startActivity(intent)
            //startActivity<RestaurantActivity>("RESTAURANT" to intent)

        }

    }

    companion object {

        @JvmStatic
        fun newInstance() = HomeFragment()
    }

    override fun onResume() {
        super.onResume()
        checkLocationPermissionAndGps()
    }


    private fun checkLocationPermissionAndGps() {
        Dexter.withActivity(activity).withPermissions(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        ).withListener(object : MultiplePermissionsListener {
            override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                if (report!!.areAllPermissionsGranted()) {

                    manager =
                        activity!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                    if (!manager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        displayPromptForEnablingGPS(activity)
                    } else {
                        val locationHelper = LocationHelper()
                        locationHelper.LocationInitialize(context)

                        Handler().postDelayed({
                            userRestaurantVM.getUserRestaurantList(
                                AppConstants_Java.lat,
                                AppConstants_Java.lng,
                                ""
                            )
                        }, 3000)


                    }
                } else {
                    Toast.makeText(
                        activity,
                        "Please allow permission to continue",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onPermissionRationaleShouldBeShown(
                permissions: MutableList<PermissionRequest>?,
                token: PermissionToken?
            ) {
                token?.continuePermissionRequest()
            }

        }).check()
    }

    // **** geofence location enable prompt
    fun displayPromptForEnablingGPS(activity: Activity?) {
        try {
            val builder = AlertDialog.Builder(activity)
            val action = Settings.ACTION_LOCATION_SOURCE_SETTINGS
            val message = "Please enable your device's GPS to continue"
            builder.setMessage(message)
                .setPositiveButton("OK") { d, id ->
                    d.dismiss()
                    requireActivity().startActivity(Intent(action))
                    d.dismiss()
                }
                .setNegativeButton("Cancel") { d, id ->
                    d.dismiss()
                }
            builder.create().show()
        } catch (e: Exception) {
        }

    }
}