package com.androidfood.mvvm.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.androidfood.mvvm.R
import com.androidfood.mvvm.data.model.DrawerItem
import com.androidfood.mvvm.databinding.HomeDrawerListItemBinding

/**
 * Created by Daniyal Ahmed on 11/16/2020.
 */
class DrawerItemAdapter(
    private val drawerItems: List<DrawerItem>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<DrawerItemAdapter.ItemViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(item: DrawerItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(parent)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(drawerItems[position], listener)
    }

    override fun getItemCount() = drawerItems.size

    inner class ItemViewHolder(
        private val parent: ViewGroup,
        private val binding: HomeDrawerListItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.home_drawer_list_item,
            parent,
            false
        )
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            item: DrawerItem,
            listener: OnItemClickListener
        ) {
            binding.drawerItem = item
            binding.root.setOnClickListener { listener.onItemClick(item) }
        }
    }
}