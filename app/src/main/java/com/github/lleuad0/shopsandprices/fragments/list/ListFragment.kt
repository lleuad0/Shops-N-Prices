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
import com.github.lleuad0.shopsandprices.R
import com.github.lleuad0.shopsandprices.databinding.FragmentListBinding
import dagger.hilt.android.AndroidEntryPoint

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

        productsList = loadFromDatabase()
        arrayAdapter = ArrayAdapter<Any?>(
            requireContext(),
            android.R.layout.simple_list_item_1,
            productsList!! as List<Any?>
        )
        binding?.productsListView?.adapter = arrayAdapter

        val productListener = ProductListener(requireContext())
        binding?.productsListView?.onItemClickListener = productListener
        binding?.productsListView?.onItemLongClickListener = productListener
    }

    private fun loadFromDatabase(): java.util.ArrayList<String> {
        sqLiteDatabase =
            requireActivity().openOrCreateDatabase("Prices", Context.MODE_PRIVATE, null)
        sqLiteDatabase!!.execSQL("CREATE TABLE IF NOT EXISTS Prices (Product VARCHAR, Price INT, Shop VARCHAR)")
        val cursor = sqLiteDatabase!!.rawQuery("SELECT * FROM Prices", null)
        cursor.moveToFirst()
        val productIndex = cursor.getColumnIndex("Product")
        val products = java.util.ArrayList<String>()
        while (!cursor.isAfterLast) {
            products.add(cursor.getString(productIndex))
            cursor.moveToNext()
        }
        cursor.close()
        if (products.isEmpty()) {
            showEmptyMessage()
        }
        return products
    }

    private fun addProduct() {
        if (productsList!!.isEmpty()) {
            showProductsList()
        }
        val newProductName = "test product " + testCount++
        val newProductPrice = "test price"
        val newProductShop = "test shop"
        val sqLiteStatement =
            sqLiteDatabase!!.compileStatement("INSERT INTO Prices (Product, Price, Shop) VALUES (?,?,?)")
        sqLiteStatement.bindString(1, newProductName)
        sqLiteStatement.bindString(2, newProductPrice)
        sqLiteStatement.bindString(3, newProductShop)
        sqLiteStatement.execute()
        productsList!!.add(newProductName)
        arrayAdapter!!.notifyDataSetChanged()
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