package com.example.network

import com.example.network.model.Category
import com.example.network.model.Product
import com.example.network.model.Tag
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface FoodieApi {
    @GET("Categories.json")
    suspend fun getCategories(): List<Category>
    @GET("Tags.json")
    suspend fun getTags(): List<Tag>
    @GET("Products.json")
    suspend fun getProducts(): List<Product>
}