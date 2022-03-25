package com.github.lleuad0.shopsandprices.fragments.list

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.AdapterView.OnItemLongClickListener
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.github.lleuad0.shopsandprices.R
import com.github.lleuad0.shopsandprices.databinding.FragmentListBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ListFragment : Fragment() {
    private var binding: FragmentListBinding? = null
    private val viewModel: ListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.addProductButton?.let {
            it.setImageResource(R.drawable.ic_plus)
            it.setOnClickListener { findNavController().navigate(R.id.editFromList) }
        }

        lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.stateFlow.collectLatest {
                    val arrayAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, it.products)
                    binding?.productsListView?.adapter = arrayAdapter

                    if (it.products.isEmpty()){
                        showEmptyMessage()
                    }
                    else{
                        showProductsList()
                    }
                }
            }
        }
        viewModel.getAllData()

        val productListener = ProductListener(requireContext())
        binding?.productsListView?.onItemClickListener = productListener
        binding?.productsListView?.onItemLongClickListener = productListener
    }

    class ProductListener(private val context: Context) : OnItemClickListener,
        OnItemLongClickListener {
        override fun onItemClick(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
        }

        override fun onItemLongClick(
            parent: AdapterView<*>?,
            view: View,
            position: Int,
            id: Long
        ): Boolean {
            (view as TextView).text.toString()
            val builder = AlertDialog.Builder(context)
            builder.setTitle(context.getString(R.string.del_alert_title))
                .setMessage(context.getString(R.string.del_alert_message))
                .setPositiveButton(
                    context.getString(R.string.del_alert_positive)
                ) { _, _ ->
                    makeToast(context.getString(R.string.deleted_success))
                }
                .setNegativeButton(context.getString(R.string.del_alert_negative), null)
            val dialog = builder.create()
            dialog.show()
            return true
        }

        private fun makeToast(content: String?) {
            Toast.makeText(context, content, Toast.LENGTH_SHORT).show()
        }
    }

    private fun showEmptyMessage() {
        binding?.noItemsTextView?.visibility = View.VISIBLE
        binding?.productsListView?.visibility = View.GONE
    }

    private fun showProductsList() {
        binding?.noItemsTextView?.visibility = View.GONE
        binding?.productsListView?.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}