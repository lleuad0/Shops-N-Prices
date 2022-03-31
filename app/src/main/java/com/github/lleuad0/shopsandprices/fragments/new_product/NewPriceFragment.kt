package com.github.lleuad0.shopsandprices.fragments.new_product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.github.lleuad0.shopsandprices.R
import com.github.lleuad0.shopsandprices.databinding.FragmentNewPriceBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NewPriceFragment : Fragment() {
    var binding: FragmentNewPriceBinding? = null
    private val navViewModel: NewProductNavViewModel by navGraphViewModels(R.id.newProductNavigation)
    private val viewModel by viewModels<NewPriceViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewPriceBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.saveButton?.setOnClickListener {
            viewModel.addPrice(
                navViewModel.stateFlow.value.product!!,
                navViewModel.stateFlow.value.shop!!,
                binding?.priceText?.text.toString().toDouble()
            )
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.stateFlow.collectLatest {
                    if (it.isPriceAdded) {
                        findNavController().navigate(NewPriceFragmentDirections.finishAdding())
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}