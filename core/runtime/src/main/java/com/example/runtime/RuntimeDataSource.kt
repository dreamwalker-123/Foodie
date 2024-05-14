package com.example.runtime

import com.example.model.Product
import kotlinx.coroutines.flow.Flow

interface RuntimeDataSource {
    val cart: Flow<Map<Product, Int>>
    fun addProduct(product: Product)
    fun removeProduct(product: Product)
    fun getQuantityProductById(id: Int): Flow<Int>
}