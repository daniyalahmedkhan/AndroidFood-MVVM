package com.androidfood.mvvm.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.androidfood.mvvm.R
import com.androidfood.mvvm.callback.ItemPositionCallback
import com.androidfood.mvvm.data.model.AddFood

class AddFoodItemsAdapter constructor(val addFood: List<AddFood> , val itemPositionCallback: ItemPositionCallback): RecyclerView.Adapter<AddFoodItemsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.add_food_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return addFood.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(addFood[position])

        holder.imageRemove.setOnClickListener {
            itemPositionCallback.ClickedItemPosition(position)
        }
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val img: ImageView = itemView.findViewById(R.id.imageItem)
        val imageRemove: ImageView = itemView.findViewById(R.id.imageRemove)
        fun bindItems(addFood: AddFood){
            img.setImageBitmap(addFood.img)
        }
    }
}