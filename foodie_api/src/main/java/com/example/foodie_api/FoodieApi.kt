package com.example.foodie_api

import com.example.foodie_api.model.Category
import com.example.foodie_api.model.Product
import com.example.foodie_api.model.Tag
import retrofit2.http.GET

interface FoodieApi {
    @GET("Categories.json")
    suspend fun getCategories(): List<Category>
    @GET("Tags.json")
    suspend fun getTags(): List<Tag>
    @GET("Products.json")
    suspend fun getProducts(): List<Product>
}