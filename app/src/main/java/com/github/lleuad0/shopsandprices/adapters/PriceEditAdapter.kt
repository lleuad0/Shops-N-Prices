package com.github.lleuad0.shopsandprices.adapters

import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.github.lleuad0.shopsandprices.databinding.ItemPriceEditBinding
import com.github.lleuad0.shopsandprices.domain.model.Price
import com.github.lleuad0.shopsandprices.domain.model.Shop

class PriceEditAdapter : RecyclerView.Adapter<PriceEditAdapter.ViewHolder>() {
    private val data: MutableList<Price> = mutableListOf()
    private var changedData: MutableList<Price> = mutableListOf()
    private var addedItemId: Long = 0

    class ViewHolder(val binding: ItemPriceEditBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPriceEditBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        data[position].let {
            holder.binding.shopName.text = SpannableStringBuilder(it.shop.name)
            holder.binding.price.text = SpannableStringBuilder(it.price.toString())
        }
        var newPrice: Price
        holder.binding.shopName.addTextChangedListener {
            it?.let {
                newPrice =
                    Price(
                        holder.binding.price.text.toString().toDouble(),
                        Shop(
                            it.toString(),
                            data[position].shop.additionalInfo,
                            data[position].shop.id
                        )
                    )
                changedData =
                    changedData.filterNot { p -> p.shop.id == newPrice.shop.id }.toMutableList()
                changedData.add(newPrice)
            }
        }
        holder.binding.price.addTextChangedListener {
            it?.let {
                newPrice =
                    Price(
                        it.toString().toDouble(), Shop(
                            holder.binding.shopName.text.toString(),
                            data[position].shop.additionalInfo,
                            data[position].shop.id
                        )
                    )
                changedData =
                    changedData.filterNot { p -> p.shop.id == newPrice.shop.id }.toMutableList()
                changedData.add(newPrice)
            }
        }
        holder.binding.deleteButton.setOnClickListener {
            changedData.add(Price(-1.0, data[position].shop))
            data.removeAt(position)
            notifyItemRemoved(position)
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

    fun exportChangedData(): List<Price> = changedData.toList()

    fun addNewPrice() {
        data.add(Price(0.0, Shop("", "", --addedItemId)))
        notifyItemInserted(data.lastIndex)
    }
}