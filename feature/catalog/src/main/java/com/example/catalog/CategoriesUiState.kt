package com.example.catalog

import com.example.foodie_api.model.CategoriesItem

interface CategoriesUiState {
    data class Success(val categories: List<CategoriesItem>) : CategoriesUiState
    data object Empty : CategoriesUiState
    data object Error : CategoriesUiState
    data object Loading : CategoriesUiState
}