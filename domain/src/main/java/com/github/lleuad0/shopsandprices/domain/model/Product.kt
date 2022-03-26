package com.github.lleuad0.shopsandprices.domain.model

data class Product(val name: String, val id: Int = 0) {

    override fun toString() = name
}