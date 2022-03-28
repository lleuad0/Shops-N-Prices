package com.github.lleuad0.shopsandprices

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.github.lleuad0.shopsandprices.databinding.ItemPriceBinding
import com.github.lleuad0.shopsandprices.domain.model.Price

class PriceAdapter : RecyclerView.Adapter<PriceAdapter.ViewHolder>() {
    private val data: MutableList<Price> = mutableListOf()

    class ViewHolder(val binding: ItemPriceBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPriceBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        data[position].let {
            holder.binding.shopName.text = it.shop.name
            holder.binding.price.text = it.price.toString()
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun setData(newData: List<Price>) {
        DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize(): Int {
                return data.size
            }

            override fun getNewListSize(): Int {
                return newData.size
            }

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return data[oldItemPosition] == newData[newItemPosition]
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return data[oldItemPosition] == newData[newItemPosition]
            }
        }).dispatchUpdatesTo(this)
        data.clear()
        data.addAll(newData)
    }
}