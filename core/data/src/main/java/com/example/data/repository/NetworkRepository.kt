package com.example.data.repository

import com.example.foodie_api.RetrofitClient
import com.example.foodie_api.model.Category
import com.example.foodie_api.model.Product
import com.example.foodie_api.model.Tag
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkRepository @Inject constructor(
    private val networkDataSource: RetrofitClient
) {
    suspend fun getCategories(): Result<List<Category>> = runCatching {
        networkDataSource.getCategories()
    }
    suspend fun getTags(): Result<List<Tag>> = runCatching {
        networkDataSource.getTags()
    }
    suspend fun getProducts(): Result<List<Product>> = runCatching {
        networkDataSource.getProducts()
    }
}