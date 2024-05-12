package com.example.catalog

import com.example.network.model.Product

interface ProductsUiState {
    data class Success(val product: Map<com.example.network.model.Product, Int>): ProductsUiState
    data object Empty : ProductsUiState
    data class Error(val error: Throwable) : ProductsUiState
    data object Loading : ProductsUiState
}