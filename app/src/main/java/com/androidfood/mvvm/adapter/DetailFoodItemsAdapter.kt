package com.androidfood.mvvm.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.androidfood.mvvm.R
import com.androidfood.mvvm.callback.ItemPositionCallback
import com.androidfood.mvvm.data.model.menus.GetMenus

class DetailFoodItemsAdapter constructor(
    val addFood: List<GetMenus>,
    val itemPositionCallback: ItemPositionCallback
) : RecyclerView.Adapter<DetailFoodItemsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.detail_restaurant, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return addFood.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(addFood[position])

    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val img: ImageView = itemView.findViewById(R.id.imageItem)
        fun bindItems(addFood: GetMenus) {
            Glide.with(itemView)  //2
                .load(addFood.image_url) //3
                .centerCrop() //4
                .placeholder(R.drawable.app_logo) //5
                .error(R.drawable.bg_primary) //6
                .into(img) //8
        }
    }
}