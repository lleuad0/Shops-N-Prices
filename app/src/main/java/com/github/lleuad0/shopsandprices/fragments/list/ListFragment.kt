package com.github.lleuad0.shopsandprices.fragments.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.lleuad0.shopsandprices.ProductListAdapter
import com.github.lleuad0.shopsandprices.R
import com.github.lleuad0.shopsandprices.databinding.FragmentListBinding
import com.github.lleuad0.shopsandprices.domain.model.Product
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ListFragment : Fragment() {
    private var binding: FragmentListBinding? = null
    private val viewModel: ListViewModel by viewModels()
    private val adapter = ProductListAdapter({
        findNavController().navigate(ListFragmentDirections.showInfoFromList(it.id))
    }) { deleteProduct(it) }

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
        binding?.productsRecyclerView?.let {
            it.layoutManager = LinearLayoutManager(requireContext())
            it.adapter = adapter
        }

        lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.stateFlow.collectLatest {
                    it.products.collectLatest { data -> adapter.submitData(data) }
                    if (it.isProductDeleted) {
                        Toast.makeText(
                            context,
                            requireContext().resources.getString(R.string.deleted_success),
                            Toast.LENGTH_SHORT
                        ).show()
                        viewModel.onDeleted()
                    }
                }
            }
        }
        lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                adapter.loadStateFlow.collectLatest {
                    when {
                        it.prepend.endOfPaginationReached && adapter.itemCount == 0 -> {
                            showEmptyMessage()
                        }
                        it.refresh is LoadState.Error -> {
                            showEmptyMessage()
                        }
                        else -> {
                            showProductsList()
                        }
                    }
                }

            }
        }
        viewModel.getAllData()
    }

    private fun showEmptyMessage() {
        binding?.noItemsTextView?.visibility = View.VISIBLE
        binding?.productsRecyclerView?.visibility = View.GONE
    }

    private fun showProductsList() {
        binding?.noItemsTextView?.visibility = View.GONE
        binding?.productsRecyclerView?.visibility = View.VISIBLE
    }

    private fun deleteProduct(product: Product) {
        viewModel.deleteProduct(product)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding?.productsRecyclerView?.adapter = null
        binding = null
    }
}