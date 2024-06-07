package com.example.data.repository

import com.example.data.utils.asInternalModel
import com.example.model.Category
import com.example.model.Product
import com.example.model.Tag
import com.example.network.retrofit.RetrofitClient
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
                .map { networkProduct -> networkProduct.asInternalModel() }
                .associateWith { product -> cart.getOrDefault(product, 0) }
        }
    }

    suspend fun getCategories(): Result<List<Category>> = runCatching {
        networkDataSource.getCategories().map { it.asInternalModel() }
    }

    suspend fun getTags(): Result<List<Tag>> = runCatching {
        networkDataSource.getTags().map { it.asInternalModel() }
    }

    fun getProductById(id: Int): Flow<Result<Pair<Product, Int>?>> = combine(
        flow { emit(runCatching { networkDataSource.getProductById(id) }) },
        runtimeDataSource.getQuantityProductById(id)
    ) { result, quantity ->
        result.map { networkProduct ->
            networkProduct?.let {
                it.asInternalModel() to quantity
            }
        }
    }

    fun addProductInCart(networkProduct: Product) = runtimeDataSource.addProduct(networkProduct)

    fun removeProductFromCart(networkProduct: Product) = runtimeDataSource.removeProduct(networkProduct)
}