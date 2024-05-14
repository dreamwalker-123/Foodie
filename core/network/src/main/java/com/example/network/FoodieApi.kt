package com.example.network


import com.example.network.model.NetworkCategory
import com.example.network.model.NetworkProduct
import com.example.network.model.NetworkTag
import retrofit2.http.GET

interface FoodieApi {
    @GET("Categories.json")
    suspend fun getCategories(): List<NetworkCategory>
    @GET("Tags.json")
    suspend fun getTags(): List<NetworkTag>
    @GET("Products.json")
    suspend fun getProducts(): List<NetworkProduct>
}