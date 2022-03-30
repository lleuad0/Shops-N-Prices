package com.github.lleuad0.shopsandprices.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.lleuad0.shopsandprices.databinding.ItemPriceAddBinding

class AddPriceAdapter(private val onClickListener: View.OnClickListener) :
    RecyclerView.Adapter<AddPriceAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemPriceAddBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemPriceAddBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.addPriceButton.setOnClickListener(onClickListener)
    }

    override fun getItemCount(): Int {
        return 1
    }
}