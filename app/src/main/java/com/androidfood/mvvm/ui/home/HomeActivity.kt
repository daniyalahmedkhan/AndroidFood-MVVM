package com.androidfood.mvvm.ui.home

import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidfood.mvvm.R
import com.androidfood.mvvm.adapter.DrawerItemAdapter
import com.androidfood.mvvm.base.BaseActivity
import com.androidfood.mvvm.callback.ActionCallback
import com.androidfood.mvvm.constants.AppConstants_Java
import com.androidfood.mvvm.data.model.DrawerItem
import com.androidfood.mvvm.data.remote.GeneralApiResponse
import com.androidfood.mvvm.databinding.ActivityHomeBinding
import com.androidfood.mvvm.helper.GeneralHelper
import com.androidfood.mvvm.helper.PrefsHelper
import com.androidfood.mvvm.ui.auth.AuthActivity
import com.androidfood.mvvm.ui.profile.ProfileActivity
import com.androidfood.mvvm.ui.profile.ProfileSetting
import com.androidfood.mvvm.ui.profile.SavedSearchActivity
import com.androidfood.mvvm.ui.restaurant.AddFoodActivity
import com.androidfood.mvvm.ui.restaurant.AddRestaurant
import com.androidfood.mvvm.vm.auth.LoginViewModel
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.fragment_home.*
import org.jetbrains.anko.startActivity
import java.util.*

class HomeActivity : BaseActivity(), ActionCallback {

    private val drawerList = listOf(
        DrawerItem(0, "Home"),
        DrawerItem(1, "Sell My Food"),
        DrawerItem(2, "Add Food"),
        DrawerItem(3, "Find Food"),
        DrawerItem(4, "My Favorites Ads"),
        DrawerItem(5, "My Saved Searches"),
        DrawerItem(6, "Account Settings"),
        DrawerItem(7, "About Us"),
        DrawerItem(8, "Profile"),
        DrawerItem(9, "Support"),
        DrawerItem(10, "Terms & Conditions"),
        DrawerItem(11, "Privacy Policy"),
        DrawerItem(12, "Sign Out")
    )
    private val binding: ActivityHomeBinding by binding(R.layout.activity_home)

    val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDrawer()
        initDrawerRecycler()
        defaultFragment(false, true, HomeFragment.newInstance())
        binding.callback = this
        binding.homeToolbar.callback = this

        binding.homeToolbar.homeTbTitle.text = "Home"


        loginViewModel.logoutState.observe(this, Observer {
            when (it) {

                is GeneralApiResponse.Loading -> {
                }
                is GeneralApiResponse.Failure -> {
                }
                is GeneralApiResponse.Success<Objects> -> {
                    PrefsHelper.putBoolean(AppConstants_Java.isLogin, false)
                    startActivity<AuthActivity>()
                    finish()
                }

            }

        })

        home_drawer_name.text = GeneralHelper.getUsersData().data.user.name
        home_drawer_email.text = GeneralHelper.getUsersData().data.user.email
    }


    private fun defaultFragment(add: Boolean, clear: Boolean, fragment: Fragment) {
        replaceFragment(
            fragment,
            R.id.home_container,
            addBackStack = add,
            clearBackStack = clear
        )
    }

    private fun initDrawer() {
        val toggle = ActionBarDrawerToggle(
            this,
            binding.homeDrawerLayout,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        binding.homeDrawerLayout.addDrawerListener(toggle)
    }

    private fun initDrawerRecycler() {
        binding.homeDrawerRv.apply {
            layoutManager =
                LinearLayoutManager(this@HomeActivity, LinearLayoutManager.VERTICAL, false)
            hasFixedSize()
            adapter = DrawerItemAdapter(drawerList, drawerItemClick)
        }
    }

    private val drawerItemClick = object : DrawerItemAdapter.OnItemClickListener {
        override fun onItemClick(item: DrawerItem) {
            binding.homeDrawerLayout.closeDrawers()
            if (item.id == 1) {
                startActivity<AddRestaurant>()
            } else if (item.id == 2) {
                startActivity<AddFoodActivity>()
            } else if (item.id == 3) {
                startActivity<FindFoodActivity>()
            } else if (item.id == 4) {
                startActivity<MyFavorites>()
            } else if (item.id == 5) {
                startActivity<SavedSearchActivity>()
            } else if (item.id == 6) {
                startActivity<ProfileSetting>()
            } else if (item.id == 7) {
                startActivity<AboutActivity>()
            } else if (item.id == 8) {
                startActivity<ProfileActivity>()
            } else if (item.id == 10) {
                startActivity<TermsNConditionActivity>()
            } else if (item.id == 11) {
                startActivity<PrivacyPolicyActivity>()
            } else if (item.id == 12) {
                loginViewModel.logout()

            }
        }
    }

    override fun onBindingClick(view: View) {
        when (view.id) {
            R.id.home_drawer_edit -> {
                binding.homeDrawerLayout.closeDrawers()
            }
            R.id.home_drawer_close -> {
                binding.homeDrawerLayout.closeDrawers()
            }
            R.id.home_tb_menu_icon -> {
                binding.homeDrawerLayout.openDrawer(Gravity.LEFT)
            }
            R.id.home_tb_search_icon -> {
                binding.homeDrawerLayout.closeDrawers()
                home_frg_search.visibility = View.VISIBLE
            }
            R.id.home_tb_filter -> {
                binding.homeDrawerLayout.closeDrawers()
                home_frg_filter.visibility = View.VISIBLE
            }
        }
    }

    override fun onBindingClick(id: Int) {
        when (id) {
            1 -> {
                binding.homeDrawerLayout.closeDrawers()
                if (home_frg_search.visibility == View.VISIBLE) home_frg_search.visibility =
                    View.GONE else home_frg_search.visibility = View.VISIBLE

            }
        }
    }
}