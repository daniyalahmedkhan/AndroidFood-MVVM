package com.androidfood.mvvm.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.androidfood.mvvm.R
import com.androidfood.mvvm.data.model.searches.SearchResponseList

class SearchItemsAdapter constructor(val searchItems: List<SearchResponseList>) :
    RecyclerView.Adapter<SearchItemsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.saved_search_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return searchItems.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(searchItems[position])
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(searchItems: SearchResponseList) {
            val itemName = itemView.findViewById(R.id.searchItem_name) as TextView
            val itemDate = itemView.findViewById(R.id.searchItem_time) as TextView

            itemName.text = searchItems.keyword
            itemDate.text = searchItems.updated_at
        }
    }
}