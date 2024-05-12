package com.example.runtime

import com.example.network.model.Product
import kotlinx.coroutines.flow.Flow

interface RuntimeDataSource {
    val cart: Flow<Map<com.example.network.model.Product, Int>>
    fun addProduct(product: com.example.network.model.Product)
    fun removeProduct(product: com.example.network.model.Product)
    fun getQuantityProductById(id: Int): Flow<Int>
}