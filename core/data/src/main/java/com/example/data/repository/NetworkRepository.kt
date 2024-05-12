package com.example.data.repository

import com.example.network.RetrofitClient
import com.example.network.model.Category
import com.example.network.model.Product
import com.example.runtime.Cart
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkRepository @Inject constructor(
    private val networkDataSource: RetrofitClient,
    private val runtimeDataSource: Cart,
) {
    fun getCart(): Flow<Map<Product, Int>> = runtimeDataSource.cart

    fun getProducts(): Flow<Result<Map<Product, Int>>> = combine(
        flow { emit(runCatching { networkDataSource.getProducts() }) },
        getCart()
    ) { result, cart ->
        result.map { networkProductsList ->
            networkProductsList
                .associateWith { product -> cart.getOrDefault(product, 0) }
        }
    }

    suspend fun getCategories(): Result<List<Category>> = runCatching {
        networkDataSource.getCategories().map { it }
    }

    fun getProductById(id: Int): Flow<Result<Pair<Product, Int>?>> = combine(
        flow { emit(runCatching { networkDataSource.getProductById(id) }) },
        runtimeDataSource.getQuantityProductById(id)
    ) { result, quantity ->
        result.map { networkProduct ->
            networkProduct?.let {
                it to quantity
            }
        }
    }

    fun addProductInCart(product: Product) = runtimeDataSource.addProduct(product)

    fun removeProductFromCart(product: Product) = runtimeDataSource.removeProduct(product)
}