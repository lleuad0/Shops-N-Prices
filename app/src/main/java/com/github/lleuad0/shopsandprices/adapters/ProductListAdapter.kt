package com.github.lleuad0.shopsandprices.adapters


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.github.lleuad0.shopsandprices.R
import com.github.lleuad0.shopsandprices.databinding.ItemListBinding
import com.github.lleuad0.shopsandprices.domain.model.Product


class ProductListAdapter(
    val onItemClick: (Product) -> Unit,
    val onItemLongClick: (Product) -> Unit
) :
    PagingDataAdapter<Product, ProductListAdapter.ViewHolder>(object :
        DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }) {

    class ViewHolder(val binding: ItemListBinding) : RecyclerView.ViewHolder(binding.root)

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemListBinding.inflate(LayoutInflater.from(parent.context))
        context = parent.context
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let {
            holder.binding.productName.text = it.name
            holder.binding.root.setOnClickListener { _ -> onItemClick(it) }
            holder.binding.root.setOnLongClickListener { _ ->

                AlertDialog.Builder(context).run {
                    setTitle(context.resources.getString(R.string.del_alert_title))
                        .setMessage(context.resources.getString(R.string.del_alert_message))
                        .setPositiveButton(
                            context.resources.getString(R.string.del_alert_positive)
                        ) { dialog, _ ->
                            dialog.cancel()
                            onItemLongClick(it)
                        }
                        .setNegativeButton(
                            context.resources.getString(R.string.del_alert_negative),
                            null
                        )
                        .create()
                        .show()
                }
                true
            }
        }
    }
}