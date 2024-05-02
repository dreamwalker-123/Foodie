package com.example.catalog

import com.example.foodie_api.model.CategoriesItem
import com.example.foodie_api.model.ProductsItem

data class CatalogUiState(
    val products: List<ProductsItem>?,
    val categories: CategoriesUiState.Loading,
    val tags: List<CategoriesItem>?,
)
