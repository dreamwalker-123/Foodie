package com.example.network.model

import kotlinx.serialization.Serializable

@Serializable
data class NetworkCategory(
    val id: Int = 0,
    val name: String = "Парам пам пам"
)
