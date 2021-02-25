package com.androidfood.mvvm.ui.restaurant

import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.Nullable
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.androidfood.mvvm.R
import com.androidfood.mvvm.adapter.AddFoodItemsAdapter
import com.androidfood.mvvm.adapter.AddRestaurantListAdapter
import com.androidfood.mvvm.base.BaseActivity
import com.androidfood.mvvm.callback.ActionCallback
import com.androidfood.mvvm.callback.ItemPositionCallback
import com.androidfood.mvvm.constants.AppConstants_Java
import com.androidfood.mvvm.data.model.AddFood
import com.androidfood.mvvm.data.model.Restaurant.AddRestaurantItems
import com.androidfood.mvvm.data.model.Restaurant.AddRestaurantResponse
import com.androidfood.mvvm.data.remote.GeneralApiResponse
import com.androidfood.mvvm.databinding.ActivityAddRestaurantBinding
import com.androidfood.mvvm.helper.GeneralHelper
import com.androidfood.mvvm.helper.PrefsHelper
import com.androidfood.mvvm.utils.ItemIndicator
import com.androidfood.mvvm.vm.restaurant_menu.RestaurantMenuViewModel
import com.pakdev.easypicker.utils.EasyImagePicker
import kotlinx.android.synthetic.main.activity_add_restaurant.*
import kotlinx.android.synthetic.main.category_sheet.*
import kotlinx.android.synthetic.main.info_sheet.*
import kotlinx.android.synthetic.main.picture_sheet.*
import kotlinx.android.synthetic.main.price_sheet.*
import kotlinx.android.synthetic.main.restaurant_bottom_sheet.*
import kotlinx.android.synthetic.main.services_sheet.RV_Services
import kotlinx.android.synthetic.main.single_restaurant.*
import kotlinx.android.synthetic.main.time_sheet.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import java.io.File
import java.util.*


class AddRestaurant : BaseActivity(), ActionCallback, ItemPositionCallback {
    val binding: ActivityAddRestaurantBinding by binding(R.layout.activity_add_restaurant)
    var isCamera: Boolean = false
    lateinit var addFoodItemsAdapter: AddFoodItemsAdapter
    var addFood: MutableList<AddFood> = ArrayList()
    private var file: ArrayList<File> = ArrayList()
    private var fileMulti: ArrayList<MultipartBody.Part> = ArrayList()
    var fileCounter: Int = 0

    // sending param
    var category: Int = -1
    var name: String = ""
    var description: String = ""
    var phone: String = ""
    var address: String = ""
    var latitude: String = ""
    var longitude: String = ""
    var url: String = ""
    var timing: String = ""
    var price_range: String = ""
    var services: String = ""
    var resID = 0

    val restaurantMenuViewModel: RestaurantMenuViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.callback = this
        binding.aboutToolbar.callback = this
        binding.aboutToolbar.titleTbTxt.text = "Sell My Food"

        binding.aboutToolbar.titleTbArrow.setOnClickListener {
            finish()
        }

        val layout = findViewById<LinearLayout>(R.id.bottom_sheet)
        val sheetBehavior = BottomSheetBehavior.from(layout);

        binding.category.setOnClickListener {
            categorylayout.visibility = View.VISIBLE
            pricesheet.visibility = View.GONE
            timesheet.visibility = View.GONE
            servicesheet.visibility = View.GONE
            pictures_sheet.visibility = View.GONE
            urlsheet.visibility = View.GONE
            if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            } else {
                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        }

        binding.price.setOnClickListener {
            categorylayout.visibility = View.GONE
            pricesheet.visibility = View.VISIBLE
            timesheet.visibility = View.GONE
            servicesheet.visibility = View.GONE
            pictures_sheet.visibility = View.GONE
            urlsheet.visibility = View.GONE
            if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            } else {
                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        }

        binding.timing.setOnClickListener {
            categorylayout.visibility = View.GONE
            pricesheet.visibility = View.GONE
            timesheet.visibility = View.VISIBLE
            servicesheet.visibility = View.GONE
            pictures_sheet.visibility = View.GONE
            urlsheet.visibility = View.GONE
            if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            } else {
                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        }

        binding.services.setOnClickListener {
            categorylayout.visibility = View.GONE
            pricesheet.visibility = View.GONE
            timesheet.visibility = View.GONE
            servicesheet.visibility = View.VISIBLE
            pictures_sheet.visibility = View.GONE
            urlsheet.visibility = View.GONE
            if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            } else {
                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        }

        binding.location.setOnClickListener {
            val intent = Intent(this, Adress_Activity::class.java)
            startActivityForResult(intent, 0);
        }

        binding.photos.setOnClickListener {
            categorylayout.visibility = View.GONE
            pricesheet.visibility = View.GONE
            timesheet.visibility = View.GONE
            servicesheet.visibility = View.GONE
            urlsheet.visibility = View.GONE
            pictures_sheet.visibility = View.VISIBLE
            if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            } else {
                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        }

        binding.url.setOnClickListener {
            categorylayout.visibility = View.GONE
            pricesheet.visibility = View.GONE
            timesheet.visibility = View.GONE
            servicesheet.visibility = View.GONE
            pictures_sheet.visibility = View.GONE
            urlsheet.visibility = View.VISIBLE
            if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            } else {
                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        }

        galleryBtn.setOnClickListener {
            isCamera = false
            EasyImagePicker.getInstance().withContext(this, "com.makan.mana").openGallery();
        }

        cameraBtn.setOnClickListener {
            isCamera = true
            EasyImagePicker.getInstance().withContext(this, "com.makan.mana").openCamera();


        }

        RV_food.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        addFoodItemsAdapter = AddFoodItemsAdapter(addFood, this)

        RV_food.setOnFlingListener(null)
        val radius = 15
        val dotsHeight = 15
        val color = ContextCompat.getColor(this@AddRestaurant, R.color.colorPrimary)
        val color2 = ContextCompat.getColor(this@AddRestaurant, R.color.grey_15)
        RV_food.addItemDecoration(ItemIndicator(radius, radius * 2, dotsHeight, color, color2))
        PagerSnapHelper().attachToRecyclerView(RV_food)
        RV_food.adapter = addFoodItemsAdapter


        binding.addRestaurant.setOnClickListener {

            name = tl_name.editText?.text.toString()
            description = tl_description.editText?.text.toString()
            url = tl_url.editText?.text.toString()
            phone = tl_phone.editText?.text.toString()

            if (category == -1 || name == "" || description == "" || phone == "" || address == ""
                || url == "" || timing == "" || price_range == "" || services == ""
            ) {
                toast("Please fill all details")
            } else {
                val cat = RequestBody.create(
                    "multipart/form-data".toMediaTypeOrNull(),
                    category.toString()
                )
                val nam = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), name)
                val des =
                    RequestBody.create("multipart/form-data".toMediaTypeOrNull(), description)
                val phn = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), phone)
                val add = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), address)
                val lati = RequestBody.create(
                    "multipart/form-data".toMediaTypeOrNull(),
                    latitude.toString()
                )
                val long =
                    RequestBody.create(
                        "multipart/form-data".toMediaTypeOrNull(),
                        longitude.toString()
                    )
                val url = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), url)
                val tim = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), timing)
                val pri =
                    RequestBody.create("multipart/form-data".toMediaTypeOrNull(), price_range)
                val serv = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), services)
                restaurantMenuViewModel.createRestaurant(
                    cat,
                    nam,
                    des,
                    phn,
                    fileMulti,
                    add,
                    lati,
                    long,
                    url,
                    tim,
                    pri,
                    serv,
                )
            }

        }

        restaurantMenuViewModel.createResState.observe(this, Observer {
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
                is GeneralApiResponse.Success<AddRestaurantResponse> -> {
                    binding.loadingView.visibility = View.GONE
                    GeneralHelper.updateRestaurantInfo(it.data!!)
                    PrefsHelper.putBoolean(AppConstants_Java.isRestaurant, true)
                    finish()
                }
            }
        })


        if (GeneralHelper.getUsersResData() != null) {
            restaurant_header_title.text = GeneralHelper.getUsersResData()!!.data.name
            restaurant_description.text = GeneralHelper.getUsersResData()!!.data.description
            restaurant_phone.text = GeneralHelper.getUsersResData()!!.data.phone
            restaurant_address.text = GeneralHelper.getUsersResData()!!.data.address
            restaurant_timing.text = GeneralHelper.getUsersResData()!!.data.timing
            restaurant_pricing.text = GeneralHelper.getUsersResData()!!.data.price_range
            resID = GeneralHelper.getUsersResData()!!.data.id

            binding.shimmerViewContainer.visibility = View.GONE
            binding.shimmerViewContainer.stopShimmer()
            binding.selfRestaurant.visibility = View.VISIBLE
        } else if (GeneralHelper.getUsersData().data.user.restaurant != null && PrefsHelper.getBoolean(
                AppConstants_Java.isRestaurant
            )
        ) {

            restaurant_header_title.text = GeneralHelper.getUsersData().data.user.restaurant.name
            restaurant_description.text =
                GeneralHelper.getUsersData().data.user.restaurant.description
            restaurant_phone.text = GeneralHelper.getUsersData().data.user.restaurant.phone
            restaurant_address.text = GeneralHelper.getUsersData().data.user.restaurant.address
            restaurant_timing.text = GeneralHelper.getUsersData().data.user.restaurant.timing
            restaurant_pricing.text = GeneralHelper.getUsersData().data.user.restaurant.price_range
            resID = GeneralHelper.getUsersData().data.user.restaurant.id

            binding.shimmerViewContainer.visibility = View.GONE
            binding.shimmerViewContainer.stopShimmer()
            binding.selfRestaurant.visibility = View.VISIBLE
        } else {
            binding.shimmerViewContainer.visibility = View.GONE
            binding.shimmerViewContainer.stopShimmer()
            binding.selfRestaurant.visibility = View.GONE
            binding.LLAddRestaurant.visibility = View.VISIBLE
        }

        add_food_fab.setOnClickListener {
            startActivity<AddFoodActivity>()
        }

        delete_fab.setOnClickListener {
            restaurantMenuViewModel.deleteRestaurant(resID)
        }

        edit_fab.setOnClickListener {
            updateRestaurant()
        }

        restaurantMenuViewModel.deleteRestaurantState.observe(this, Observer {
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
                    GeneralHelper.updateRestaurantInfo(null)
                    PrefsHelper.putBoolean(AppConstants_Java.isRestaurant, false)
                    finish()
                }
            }
        })


        categoryLoad()
        priceLoad()
        servicesLoad()
        timeLoad()
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


    private fun categoryLoad() {

        val items = listOf<AddRestaurantItems>(
            AddRestaurantItems("Fast food"),
            AddRestaurantItems("Fast casual"),
            AddRestaurantItems("Casual dining"),
            AddRestaurantItems("Premium casual"),
            AddRestaurantItems("Family style"),
            AddRestaurantItems("Fine dining"),
            AddRestaurantItems("Brasserie and bistro"),
            AddRestaurantItems("Other")
        )
        RV_categoryItem.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        val adapter = AddRestaurantListAdapter(this, items)
        RV_categoryItem.adapter = adapter

        adapter.clickItem(object : ItemPositionCallback {
            override fun ClickedItemPosition(position: Int) {
                Toast.makeText(this@AddRestaurant, "" + items[position].item, Toast.LENGTH_SHORT)
                    .show()
                category = position + 1
                tv_cat.text = items[position].item
            }
        })

    }


    private fun priceLoad() {

        val items = listOf<AddRestaurantItems>(
            AddRestaurantItems("$50-$250"),
            AddRestaurantItems("$250-$500"),
            AddRestaurantItems("$500-$1000"),
            AddRestaurantItems("$1000-$2000")
        )
        RV_Price.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        val adapter = AddRestaurantListAdapter(this, items)
        RV_Price.adapter = adapter

        adapter.clickItem(object : ItemPositionCallback {
            override fun ClickedItemPosition(position: Int) {
                Toast.makeText(this@AddRestaurant, "" + items[position].item, Toast.LENGTH_SHORT)
                    .show()
                price_range = items[position].item
                tv_price.text = price_range

            }
        })

    }

    private fun servicesLoad() {

        val items = listOf<AddRestaurantItems>(
            AddRestaurantItems("Dine-in"),
            AddRestaurantItems("Delivery"),
            AddRestaurantItems("Take Away")
        )
        RV_Services.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        val adapter = AddRestaurantListAdapter(this, items)
        RV_Services.adapter = adapter

        adapter.clickItem(object : ItemPositionCallback {
            override fun ClickedItemPosition(position: Int) {
                Toast.makeText(this@AddRestaurant, "" + items[position].item, Toast.LENGTH_SHORT)
                    .show()
                services = items[position].item
                tv_services.text = services
            }
        })

    }

    private fun timeLoad() {


        ET_fromTime.setOnClickListener {
            // Get Current Time
            var mYear: Int
            var mMonth: Int
            var mDay: Int
            val mHour: Int
            val mMinute: Int
            // Get Current Time
            val c: Calendar = Calendar.getInstance()
            mHour = c.get(Calendar.HOUR_OF_DAY)
            mMinute = c.get(Calendar.MINUTE)

            // Launch Time Picker Dialog

            // Launch Time Picker Dialog
            val timePickerDialog = TimePickerDialog(
                this,
                OnTimeSetListener { view, hourOfDay, minute -> ET_fromTime.setText("$hourOfDay:$minute") },
                mHour,
                mMinute,
                false
            )
            timePickerDialog.show()
        }


        ET_toTime.setOnClickListener {
            // Get Current Time
            var mYear: Int
            var mMonth: Int
            var mDay: Int
            val mHour: Int
            val mMinute: Int
            // Get Current Time
            val c: Calendar = Calendar.getInstance()
            mHour = c.get(Calendar.HOUR_OF_DAY)
            mMinute = c.get(Calendar.MINUTE)

            // Launch Time Picker Dialog

            // Launch Time Picker Dialog
            val timePickerDialog = TimePickerDialog(
                this,
                OnTimeSetListener { view, hourOfDay, minute -> ET_toTime.setText("$hourOfDay:$minute") },
                mHour,
                mMinute,
                false
            )
            timePickerDialog.show()
        }

        ET_toTime.addTextChangedListener {
            tv_timing.text = ET_fromTime.text.toString() + " - " + ET_toTime.text.toString()
            timing = ET_fromTime.text.toString() + " - " + ET_toTime.text.toString()
        }

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
                latitude = data?.extras?.get("LAT").toString()
                longitude = data?.extras?.get("LNG").toString()
                TV_location.setText(add)
                address = add
                //  endPoint = add
                // endPointLatLng = loc
            }
        } else {
            EasyImagePicker.getInstance()
                .passActivityResult(requestCode, resultCode, data, object :
                    EasyImagePicker.easyPickerCallback {
                    override fun onMediaFilePicked(result: String) {

                        file.add(File(result))
                        addMultipart(File(result))
                        val myBitmap = BitmapFactory.decodeFile(file.last().getAbsolutePath())
                        addFood.add(AddFood(myBitmap))
                        addFoodItemsAdapter.notifyDataSetChanged()
                        fileCounter += 1


                    }

                    override fun onFailed(error: String) {
                        Toast.makeText(this@AddRestaurant, "Error :$error", Toast.LENGTH_SHORT)
                            .show()
                    }
                })
        }

    }

    override fun ClickedItemPosition(position: Int) {
        addFood.removeAt(position)
        addFoodItemsAdapter.notifyDataSetChanged()
        fileCounter -= 1
    }

    private fun addMultipart(f: File) {
        val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), f)
        val body =
            MultipartBody.Part.createFormData("images[]", "images", requestFile)
        fileMulti.add(body)
    }

    private fun updateRestaurant() {
        binding.selfRestaurant.visibility = View.GONE
        binding.addRestaurant.visibility = View.GONE
        binding.photos.visibility = View.GONE
        binding.LLAddRestaurant.visibility = View.VISIBLE
        binding.updateRestaurant.visibility = View.VISIBLE

        binding.updateRestaurant.setOnClickListener {
            name = tl_name.editText?.text.toString()
            description = tl_description.editText?.text.toString()
            url = tl_url.editText?.text.toString()
            phone = tl_phone.editText?.text.toString()

            if (category == -1 || name == "" || description == "" || phone == "" || address == ""
                || url == "" || timing == "" || price_range == "" || services == ""
            ) {
                toast("Please fill all details")
            } else {
                restaurantMenuViewModel.updateRestaurant(
                    resID,
                    category,
                    name,
                    description,
                    phone,
                    address,
                    latitude.toString(),
                    longitude.toString(),
                    url,
                    timing,
                    price_range,
                    services,
                )
            }
        }
    }

}