package com.example.network

import com.example.network.model.NetworkCategory
import com.example.network.model.NetworkProduct
import com.example.network.model.NetworkTag
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RetrofitClient @Inject constructor(): FoodieApi {

    private val baseUrl =
        "https://anika1d.github.io/WorkTestServer/"

    private var json = Json { ignoreUnknownKeys = true }

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(baseUrl)
        .build()
        .create(FoodieApi::class.java)

    override suspend fun getCategories(): List<NetworkCategory> {
        return retrofit.getCategories()
    }

    override suspend fun getTags(): List<NetworkTag> {
        return retrofit.getTags()
    }

    override suspend fun getProducts(): List<NetworkProduct> {
        return retrofit.getProducts()
    }
    suspend fun getProductById(id: Int): NetworkProduct? = getProducts().firstOrNull { it.id == id }
}