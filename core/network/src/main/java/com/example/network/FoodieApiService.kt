package com.example.network

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

    override suspend fun getCategories(): List<com.example.network.model.Category> {
        return retrofit.getCategories()
    }

    override suspend fun getTags(): List<com.example.network.model.Tag> {
        return retrofit.getTags()
    }

    override suspend fun getProducts(): List<com.example.network.model.Product> {
        return retrofit.getProducts()
    }
    suspend fun getProductById(id: Int): com.example.network.model.Product? = getProducts().firstOrNull { it.id == id }
}