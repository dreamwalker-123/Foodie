package com.example.network

import com.example.network.fake.FakeFoodieApiService
import com.example.network.model.NetworkCategory
import com.example.network.model.NetworkProduct
import com.example.network.model.NetworkTag
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import kotlinx.coroutines.test.runTest

class FakeFoodieApiServiceTest {
    private lateinit var subject: FakeFoodieApiService

    @Before
    fun setUp() {
        subject = FakeFoodieApiService(
            networkJson = Json { ignoreUnknownKeys = true },
        )
    }

    @Test
    fun receiving_is_correct() = runTest {
        assertEquals(subject.getProductById(0), NetworkProduct())
        assertEquals(subject.getTags(), List(10) { NetworkTag() })
        assertEquals(subject.getCategories(), List(10) { NetworkCategory() })
        assertEquals(subject.getProducts(), List(10) { NetworkProduct() })
    }
}