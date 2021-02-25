package com.androidfood.mvvm.ui.profile

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.androidfood.mvvm.R
import com.androidfood.mvvm.adapter.SearchItemsAdapter
import com.androidfood.mvvm.base.BaseActivity
import com.androidfood.mvvm.callback.ActionCallback
import com.androidfood.mvvm.data.model.searches.SearchResponse
import com.androidfood.mvvm.data.remote.GeneralApiResponse
import com.androidfood.mvvm.databinding.ActivitySavedSearchBinding
import com.androidfood.mvvm.vm.home.home.SearchViewModel
import kotlinx.android.synthetic.main.activity_saved_search.*

class SavedSearchActivity : BaseActivity(), ActionCallback {

    lateinit var searchViewModel: SearchViewModel
    lateinit var searchItemsAdapter: SearchItemsAdapter

    private val binding: ActivitySavedSearchBinding by binding(R.layout.activity_saved_search)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.aboutToolbar.titleTbTxt.text = "My Saved Search"
        binding.aboutToolbar.callback = this

        RV_savedSearch.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        searchViewModel = ViewModelProvider(this).get(SearchViewModel::class.java)


        searchViewModel.searchItem()

        searchViewModel.searchResponseState.observe(this, Observer {

            when (it) {
                is GeneralApiResponse.Success<SearchResponse> -> {

                    searchItemsAdapter = SearchItemsAdapter(it.data!!.searchList)
                    RV_savedSearch.adapter = searchItemsAdapter
                    searchItemsAdapter.notifyDataSetChanged()

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

    override fun onBindingClick(id: Int) {}


}