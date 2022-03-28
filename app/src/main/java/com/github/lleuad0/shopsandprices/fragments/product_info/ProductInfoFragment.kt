package com.github.lleuad0.shopsandprices.fragments.product_info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.lleuad0.shopsandprices.PriceAdapter
import com.github.lleuad0.shopsandprices.databinding.FragmentProductInfoBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductInfoFragment : Fragment() {
    private var binding: FragmentProductInfoBinding? = null
    private val viewModel: ProductInfoViewModel by viewModels()
    private val args by navArgs<ProductInfoFragmentArgs>()
    private val adapter = PriceAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProductInfoBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.recyclerView?.let {
            it.layoutManager = LinearLayoutManager(requireContext())
            it.adapter = adapter
        }

        lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.stateFlow.collectLatest {
                    binding?.productTitle?.text = it.product?.name
                    adapter.setData(it.shopsAndPrices)
                }
            }
        }
        args.productId.let {
            viewModel.getProduct(it)
            viewModel.getDataForProduct(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding?.recyclerView?.adapter = null
        binding = null
    }
}