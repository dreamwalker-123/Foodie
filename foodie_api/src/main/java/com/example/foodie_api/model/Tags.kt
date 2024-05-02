package com.example.foodie_api.model

import kotlinx.serialization.Serializable

@Serializable
data class Tags(val list: List<CategoriesItem>)
