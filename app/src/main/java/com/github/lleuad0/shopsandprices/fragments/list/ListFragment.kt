package com.github.lleuad0.shopsandprices.fragments.list

import android.app.AlertDialog
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import android.widget.AdapterView.OnItemLongClickListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.github.lleuad0.shopsandprices.R
import com.github.lleuad0.shopsandprices.databinding.FragmentListBinding
import com.github.lleuad0.shopsandprices.domain.Product
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ListFragment : Fragment() {
    private var binding: FragmentListBinding? = null
    private val viewModel: ListViewModel by viewModels()

    private var sqLiteDatabase: SQLiteDatabase? = null
    private var arrayAdapter: ArrayAdapter<*>? = null
    private var productsList: ArrayList<String>? = null

    private var testCount = 0

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
            it.setOnClickListener { addProduct() }
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

    private fun addProduct() {
        val newProductName = "test product " + testCount++
        val newProductPrice = 1.5
        val newProductShop = arrayListOf("test shop")
        val product = Product(newProductName,newProductPrice,newProductShop)
        viewModel.addProduct(product)
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