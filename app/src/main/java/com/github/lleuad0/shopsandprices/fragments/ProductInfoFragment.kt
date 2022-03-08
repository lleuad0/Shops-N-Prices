package com.github.lleuad0.shopsandprices.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.lleuad0.shopsandprices.databinding.FragmentEditProductBinding
import com.github.lleuad0.shopsandprices.databinding.FragmentProductInfoBinding

class ProductInfoFragment : Fragment() {
    var binding: FragmentProductInfoBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProductInfoBinding.inflate(inflater)
        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}