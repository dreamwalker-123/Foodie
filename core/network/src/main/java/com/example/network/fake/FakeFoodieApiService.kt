package com.example.network.fake

import com.example.network.FoodieApi
import com.example.network.model.NetworkCategory
import com.example.network.model.NetworkProduct
import com.example.network.model.NetworkTag
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Inject

class FakeFoodieApiService @Inject constructor(
    networkJson: Json,
): FoodieApi {

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(networkJson.asConverterFactory("application/json".toMediaType()))
        .baseUrl("https://anika1d.github.io/WorkTestServer/")
        .build()
        .create(FoodieApi::class.java)
    override suspend fun getCategories(): List<NetworkCategory> {
        return List(10) { NetworkCategory() }
    }

    override suspend fun getTags(): List<NetworkTag> {
        return List(10) { NetworkTag() }
    }

    override suspend fun getProducts(): List<NetworkProduct> {
        return List(10) { NetworkProduct() }
    }
    suspend fun getProductById(id: Int): NetworkProduct? = getProducts().firstOrNull { it.id == id }
}