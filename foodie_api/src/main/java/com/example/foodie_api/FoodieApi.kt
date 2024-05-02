package com.example.foodie_api

import com.example.foodie_api.model.Categories
import com.example.foodie_api.model.CategoriesItem
import com.example.foodie_api.model.Products
import com.example.foodie_api.model.ProductsItem
import com.example.foodie_api.model.Tags
import retrofit2.http.GET

interface FoodieApi {
    @GET("Categories.json")
    suspend fun getCategories(): List<CategoriesItem>
    @GET("Tags.json")
    suspend fun getTags(): List<CategoriesItem>
    @GET("Products.json")
    suspend fun getProducts(): List<ProductsItem>
}