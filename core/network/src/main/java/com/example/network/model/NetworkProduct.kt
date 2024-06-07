package com.example.network.model

import kotlinx.serialization.Serializable

@Serializable
data class NetworkProduct(
    val carbohydrates_per_100_grams: Double = 0.0,
    val category_id: Int = 0,
    val description: String = "Парам пам пам",
    val energy_per_100_grams: Double = 0.0,
    val fats_per_100_grams: Double = 0.0,
    val id: Int = 0,
    val image: String = "Парам пам пам",
    val measure: Int = 0,
    val measure_unit: String = "Парам пам пам",
    val name: String = "Парам пам пам",
    val price_current: Int = 0,
    val price_old: Int? = null,
    val proteins_per_100_grams: Double = 0.0,
    val tag_ids: List<Int> = emptyList()
)