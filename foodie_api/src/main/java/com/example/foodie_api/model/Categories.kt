package com.example.foodie_api.model

import kotlinx.serialization.Serializable

@Serializable
data class CategoriesItem(
    val id: Int,
    val name: String
)
