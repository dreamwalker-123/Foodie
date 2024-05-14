package com.example.network.model

import kotlinx.serialization.Serializable

@Serializable
data class NetworkCategory(
    val id: Int,
    val name: String
)
