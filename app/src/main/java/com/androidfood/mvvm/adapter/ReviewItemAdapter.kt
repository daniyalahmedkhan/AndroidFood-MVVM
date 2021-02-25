package com.androidfood.mvvm.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.androidfood.mvvm.R
import com.androidfood.mvvm.data.model.reviews.ReviewData
import com.androidfood.mvvm.databinding.ReviewListItemBinding
import com.androidfood.mvvm.helper.GeneralHelper

/**
 * Created by Daniyal Ahmed on 11/18/2020.
 */
class ReviewItemAdapter(
    private val restaurantsItems: List<ReviewData>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<ReviewItemAdapter.ItemViewHolder>() {


    interface OnItemClickListener {
        fun onItemClick(item: ReviewData)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(parent)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(restaurantsItems[position], listener)

    }

    override fun getItemCount() = restaurantsItems.size

    inner class ItemViewHolder(
        private val parent: ViewGroup,
        private val binding: ReviewListItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.review_list_item,
            parent,
            false
        )
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            item: ReviewData,
            listener: OnItemClickListener
        ) {
            binding.restaurant = item
            binding.root.setOnClickListener { listener.onItemClick(item) }
            binding.reviewItemName.text = item.user.name
            binding.reviewItemReview.text = item.review
            binding.reviewItemTime.text = item.created_at
            Glide.with(parent)  //2
                .load(item.user.details.imageUrl) //3
                .centerCrop() //4
                .placeholder(R.drawable.app_logo) //5
                .error(R.drawable.bg_primary) //6
                .into(binding.reviewItemImg) //8

            if (item.user.id.equals(GeneralHelper.getUsersData().data.user.id)) {
                binding.edit.visibility = View.VISIBLE
            } else {
                binding.edit.visibility = View.GONE

            }
        }
    }
}