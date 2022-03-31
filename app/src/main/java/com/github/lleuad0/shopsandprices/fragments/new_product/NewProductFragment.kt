package com.github.lleuad0.shopsandprices.fragments.new_product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.github.lleuad0.shopsandprices.R
import com.github.lleuad0.shopsandprices.databinding.FragmentNewProductBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NewProductFragment : Fragment() {
    var binding: FragmentNewProductBinding? = null
    private val navViewModel by navGraphViewModels<NewProductNavViewModel>(R.id.newProductNavigation)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewProductBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.nextButton?.setOnClickListener {
            navViewModel.addProduct(
                binding?.productNameInput?.text.toString(),
                binding?.productInfoInput?.text.toString(),
            )
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                navViewModel.stateFlow.collectLatest {
                    if (it.isProductAdded) {
                        findNavController().navigate(NewProductFragmentDirections.addShop())
                        navViewModel.onRedirectedProduct()
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