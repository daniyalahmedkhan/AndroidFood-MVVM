package com.androidfood.mvvm.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.androidfood.mvvm.R
import com.androidfood.mvvm.data.model.RestaurantItem
import com.androidfood.mvvm.databinding.RestaurantListItemBinding

class FavoritesItemsAdapter constructor(
    private val restaurantsItems: List<RestaurantItem>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<FavoritesItemsAdapter.ItemViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(item: RestaurantItem, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(parent)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(restaurantsItems[position], listener, position)
    }

    override fun getItemCount() = restaurantsItems.size

    inner class ItemViewHolder(
        private val parent: ViewGroup,
        private val binding: RestaurantListItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.restaurant_list_item,
            parent,
            false
        )
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            item: RestaurantItem,
            listener: OnItemClickListener,
            position: Int
        ) {
            binding.restaurant = item
            binding.restaurantItemName.text = item.name
            binding.restaurantItemDesc.text = item.desc
            binding.restaurantItemRating.rating = item.rating.toFloat()
            binding.txtRating.text = item.rating.toString()
            Glide.with(parent)  //2
                .load(item.img) //3
                .centerCrop() //4
                .placeholder(R.drawable.app_logo) //5
                .error(R.drawable.bg_primary) //6
                .into(binding.restaurantItemImg) //8
            binding.root.setOnClickListener { listener.onItemClick(item, position) }
        }
    }

}