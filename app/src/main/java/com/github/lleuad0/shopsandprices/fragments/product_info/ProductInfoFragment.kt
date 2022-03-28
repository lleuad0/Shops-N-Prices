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
import com.github.lleuad0.shopsandprices.databinding.FragmentProductInfoBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductInfoFragment : Fragment() {
    private var binding: FragmentProductInfoBinding? = null
    private val viewModel: ProductInfoViewModel by viewModels()
    private val args by navArgs<ProductInfoFragmentArgs>()

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

        lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.stateFlow.collectLatest {
                    binding?.productTitleTextView?.text = it.product?.name
                    binding?.shopTextView?.text = it.shopsAndPricesMap.keys.toString()
                    binding?.priceTextView?.text = it.shopsAndPricesMap.values.toString()
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
        binding = null
    }
}