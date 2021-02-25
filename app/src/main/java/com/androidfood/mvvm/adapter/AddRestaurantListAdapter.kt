package com.androidfood.mvvm.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.androidfood.mvvm.R
import com.androidfood.mvvm.callback.ItemPositionCallback
import com.androidfood.mvvm.data.model.Restaurant.AddRestaurantItems

class AddRestaurantListAdapter constructor(
    private val context: Context,
    private val addRestaurantItems: List<AddRestaurantItems>
) :
    RecyclerView.Adapter<AddRestaurantListAdapter.ViewHolder>() {
    private var itemPositionCallback: ItemPositionCallback? = null

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val text = itemView.findViewById(R.id.txtitem) as TextView

        fun bindItems(addRestaurantItems: AddRestaurantItems) {
            text.setText(addRestaurantItems.item)
        }
    }

    fun clickItem(itemPositionCallback: ItemPositionCallback) {
        this.itemPositionCallback = itemPositionCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.add_restaurant_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return addRestaurantItems.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bindItems(addRestaurantItems[position])

        holder.text.setOnClickListener {
            this.itemPositionCallback?.ClickedItemPosition(position)
        }
    }
}