package com.example.foodie_api

import com.example.foodie_api.model.Categories
import com.example.foodie_api.model.CategoriesItem
import com.example.foodie_api.model.Products
import com.example.foodie_api.model.ProductsItem
import com.example.foodie_api.model.Tags
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RetrofitClient @Inject constructor(): FoodieApi {

    private val baseUrl =
        "https://anika1d.github.io/WorkTestServer/"

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    private var json = Json { ignoreUnknownKeys = true }

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(baseUrl).client(okHttpClient)
        .build()
        .create(FoodieApi::class.java)

    override suspend fun getCategories(): List<CategoriesItem> {
        return retrofit.getCategories()
    }

    override suspend fun getTags(): List<CategoriesItem> {
        return retrofit.getTags()
    }

    override suspend fun getProducts(): List<ProductsItem> {
        return retrofit.getProducts()
    }
}