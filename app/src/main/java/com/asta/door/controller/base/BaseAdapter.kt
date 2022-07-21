package com.asta.door.controller.base

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

class BaseAdapter {

    abstract class Recyclerview<Model, VH : RecyclerView.ViewHolder?> : RecyclerView.Adapter<VH>() {
        protected var currentList = mutableListOf<Model>()
        var onItemClickCallback: OnItemClickCallback<Model>? = null

        fun setOnItemClickListener(callback: (clickTag: String, item: Model, position: Int) -> Unit) {
            this.onItemClickCallback = object : OnItemClickCallback<Model> {
                override fun onItemClick(clickTag: String, item: Model, position: Int) {
                    callback(clickTag, item, position)
                }
            }
        }

        @SuppressLint("NotifyDataSetChanged")
        fun submitList(list: List<Model>) {
            currentList.clear()
            currentList.addAll(list)
            notifyDataSetChanged()
        }

        override fun getItemCount() = currentList.size
    }

    abstract class ListRecyclerAdapter<Model, VH : RecyclerView.ViewHolder>(diffCallback: DiffUtil.ItemCallback<Model>) :
        ListAdapter<Model, VH>(diffCallback) {

        protected var onItemClickCallback: OnItemClickCallback<Model>? = null

        fun setOnItemClickListener(listener: (clickTag: String, model: Model, position: Int) -> Unit) {
            onItemClickCallback = object : OnItemClickCallback<Model> {
                override fun onItemClick(clickTag: String, item: Model, position: Int) {
                    listener(clickTag, item, position)
                }
            }
        }
    }

    abstract class BaseViewHolder<T>(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {
        abstract fun bind(item: T)
    }

    interface OnItemClickCallback<Item> {
        fun onItemClick(clickTag: String, item: Item, position: Int)
    }
}