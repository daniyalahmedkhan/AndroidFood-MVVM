package com.androidfood.mvvm.ui.restaurant


import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.Nullable
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.androidfood.mvvm.R
import com.androidfood.mvvm.adapter.AddFoodItemsAdapter
import com.androidfood.mvvm.base.BaseActivity
import com.androidfood.mvvm.callback.ActionCallback
import com.androidfood.mvvm.callback.ItemPositionCallback
import com.androidfood.mvvm.constants.AppConstants_Java
import com.androidfood.mvvm.data.model.AddFood
import com.androidfood.mvvm.data.remote.GeneralApiResponse
import com.androidfood.mvvm.databinding.ActivityAddFoodBinding
import com.androidfood.mvvm.helper.GeneralHelper
import com.androidfood.mvvm.helper.PrefsHelper
import com.androidfood.mvvm.utils.ItemIndicator
import com.androidfood.mvvm.vm.restaurant_menu.RestaurantMenuViewModel
import com.pakdev.easypicker.utils.EasyImagePicker
import kotlinx.android.synthetic.main.activity_add_food.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.jetbrains.anko.startActivity
import java.io.File


class AddFoodActivity : BaseActivity(), ActionCallback, ItemPositionCallback {

    var isCamera: Boolean = false
    lateinit var addFoodItemsAdapter: AddFoodItemsAdapter
    var addFood: MutableList<AddFood> = ArrayList()
    private var file: ArrayList<File> = ArrayList()

    val restaurantMenuViewModel: RestaurantMenuViewModel by viewModels()
    private val binding: ActivityAddFoodBinding by binding(R.layout.activity_add_food)
    var fileCounter: Int = 0
    var resID: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding.callback = this
        binding.aboutToolbar.titleTbTxt.text = "Add Food"
        binding.aboutToolbar.callback = this


        RV_food.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        addFoodItemsAdapter = AddFoodItemsAdapter(addFood, this)

        RV_food.setOnFlingListener(null)
        val radius = 15
        val dotsHeight = 15
        val color = ContextCompat.getColor(this@AddFoodActivity, R.color.colorPrimary)
        val color2 = ContextCompat.getColor(this@AddFoodActivity, R.color.grey_15)
        RV_food.addItemDecoration(ItemIndicator(radius, radius * 2, dotsHeight, color, color2))
        PagerSnapHelper().attachToRecyclerView(RV_food)
        RV_food.adapter = addFoodItemsAdapter


        cameraBtn.setOnClickListener {
            isCamera = true
            EasyImagePicker.getInstance().withContext(this, "com.makan.mana").openCamera();


        }

        galleryBtn.setOnClickListener {
            isCamera = false
            EasyImagePicker.getInstance().withContext(this, "com.makan.mana").openGallery();
        }

        uploadBtn.setOnClickListener {
            uploadMenu()
        }

        restaurantMenuViewModel.addMenusState.observe(this, Observer {
            when (it) {

                is GeneralApiResponse.Loading -> {
                    loadingView.visibility = View.VISIBLE
                }
                is GeneralApiResponse.Failure -> {
                    fileCounter -= 1
                    if (fileCounter <= 0) {
                        loadingView.visibility = View.GONE
                    }

                }
                is GeneralApiResponse.Success -> {
                    fileCounter -= 1
                    if (fileCounter <= 0) {
                        loadingView.visibility = View.GONE
                        startActivity<AddRestaurant>()
                        finish()
                    }


                }

            }
        })

        if (GeneralHelper.getUsersResData() != null) {
            resID = GeneralHelper.getUsersResData()!!.data.id

            LL_addFood.visibility = View.VISIBLE
            LL_noRes.visibility = View.GONE

        } else if (GeneralHelper.getUsersData().data.user.restaurant != null && PrefsHelper.getBoolean(
                AppConstants_Java.isRestaurant
            )
        ) {

            resID = GeneralHelper.getUsersData().data.user.restaurant.id

            LL_addFood.visibility = View.VISIBLE
            LL_noRes.visibility = View.GONE

        } else {
            LL_addFood.visibility = View.GONE
            LL_noRes.visibility = View.VISIBLE
        }

        LL_noRes.setOnClickListener {
            startActivity<AddRestaurant>()
            finish()
        }
    }


    override
    fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        @Nullable data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        EasyImagePicker.getInstance()
            .passActivityResult(requestCode, resultCode, data, object :
                EasyImagePicker.easyPickerCallback {
                override fun onMediaFilePicked(result: String) {

                    file.add(File(result))
                    val myBitmap = BitmapFactory.decodeFile(file.last().getAbsolutePath())
                    addFood.add(AddFood(myBitmap))
                    addFoodItemsAdapter.notifyDataSetChanged()
                    fileCounter += 1


                }

                override fun onFailed(error: String) {
                    Toast.makeText(this@AddFoodActivity, "Error :$error", Toast.LENGTH_SHORT).show()
                }
            })
    }

    override fun ClickedItemPosition(position: Int) {
        addFood.removeAt(position)
        addFoodItemsAdapter.notifyDataSetChanged()
        fileCounter -= 1
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


    private fun uploadMenu() {


        for (f in file) {

            val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), f)
            val body =
                MultipartBody.Part.createFormData("image", "image", requestFile)

            val name =
                RequestBody.create(
                    "multipart/form-data".toMediaTypeOrNull(),
                    GeneralHelper.getUsersData().data.user.name
                )
            val desc = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), "MENU")
            val id = RequestBody.create(
                "multipart/form-data".toMediaTypeOrNull(),
                "" + resID
            )

            restaurantMenuViewModel.addMenu(body, name, desc, id)
        }
    }
}