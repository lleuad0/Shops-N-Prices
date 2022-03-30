package com.github.lleuad0.shopsandprices.fragments.edit_product

import android.os.Bundle
import android.text.SpannableStringBuilder
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
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.lleuad0.shopsandprices.R
import com.github.lleuad0.shopsandprices.adapters.AddPriceAdapter
import com.github.lleuad0.shopsandprices.adapters.PriceEditAdapter
import com.github.lleuad0.shopsandprices.databinding.FragmentProductEditBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditProductFragment : Fragment() {
    private var binding: FragmentProductEditBinding? = null
    private val viewModel: EditProductViewModel by viewModels()
    private val args by navArgs<EditProductFragmentArgs>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProductEditBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val priceEditAdapter = PriceEditAdapter()
        val addPriceAdapter = AddPriceAdapter { priceEditAdapter.addNewPrice() }
        val concatAdapter = ConcatAdapter(priceEditAdapter, addPriceAdapter)

        binding?.recyclerView?.let {
            it.layoutManager = LinearLayoutManager(requireContext())
            it.adapter = concatAdapter
        }
        binding?.saveButton?.setOnClickListener {
            viewModel.saveProductData(
                binding?.productTitle?.text?.toString(),
                binding?.productInfo?.text?.toString(),
                priceEditAdapter.exportChangedData()
            )
        }

        lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.stateFlow.collectLatest {
                    binding?.productTitle?.text = SpannableStringBuilder(it.product?.name ?: "")
                    binding?.productInfo?.text =
                        SpannableStringBuilder(it.product?.additionalInfo ?: "")
                    priceEditAdapter.setData(it.shopsAndPrices)

                    if (it.isSaved) {
                        Toast.makeText(
                            requireContext(),
                            requireContext().getText(R.string.added_success),
                            Toast.LENGTH_SHORT
                        ).show()
                        findNavController().popBackStack()
                    }
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