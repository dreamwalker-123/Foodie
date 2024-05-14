package com.example.data.utils

import com.example.model.Category
import com.example.model.Product
import com.example.model.Tag
import com.example.network.model.NetworkCategory
import com.example.network.model.NetworkProduct
import com.example.network.model.NetworkTag

fun NetworkProduct.asInternalModel() = Product(
    id = id,
    category_id = category_id,
    name = name,
    description = description,
    image = image,
    price_current =  price_current,
    price_old = price_old,
    measure = measure,
    measure_unit = measure_unit,
    energy_per_100_grams = energy_per_100_grams,
    proteins_per_100_grams = proteins_per_100_grams,
    fats_per_100_grams = fats_per_100_grams,
    carbohydrates_per_100_grams = carbohydrates_per_100_grams,
    tag_ids = tag_ids,
)

fun NetworkCategory.asInternalModel() = Category(
    id = id,
    name = name,
)

fun NetworkTag.asInternalModel() = Tag(
    id = id,
    name = name,
)