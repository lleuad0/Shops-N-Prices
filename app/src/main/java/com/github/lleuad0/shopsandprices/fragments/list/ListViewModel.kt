package com.github.lleuad0.shopsandprices.fragments.list

import androidx.lifecycle.ViewModel
import com.github.lleuad0.shopsandprices.data.Database
import com.github.lleuad0.shopsandprices.data.ProductDao
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(private val dao: ProductDao): ViewModel() {
    fun getAllData(){
        dao.selectAll()
    }
}