package com.example.network.model

import kotlinx.serialization.Serializable

@Serializable
data class NetworkTag(
    val id: Int,
    val name: String
)
