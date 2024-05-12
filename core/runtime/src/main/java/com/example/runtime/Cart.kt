package com.example.runtime

import com.example.network.model.Product
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Cart @Inject constructor() : RuntimeDataSource {
    private val productsMap = mutableMapOf<com.example.network.model.Product, Int>()
    private val cartFlow = MutableSharedFlow<Map<com.example.network.model.Product, Int>>()
    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    override val cart: Flow<Map<com.example.network.model.Product, Int>> = cartFlow.onStart { emit(productsMap.toMap()) }

    override fun addProduct(product: com.example.network.model.Product) {
        productsMap.merge(product, 1) { oldValue, _ ->
            oldValue + 1
        }
        coroutineScope.launch {
            cartFlow.emit(productsMap.toMap())
        }
    }

    override fun removeProduct(product: com.example.network.model.Product) {
        productsMap.merge(product, 0) { oldValue, _ ->
            if (oldValue <= 1) {
                null
            } else {
                oldValue - 1
            }
        }
        coroutineScope.launch {
            cartFlow.emit(productsMap.toMap())
        }
    }

    override fun getQuantityProductById(id: Int): Flow<Int> = cart.map { cart ->
        cart.mapKeys { it.key.id }.getOrDefault(id, 0)
    }
}