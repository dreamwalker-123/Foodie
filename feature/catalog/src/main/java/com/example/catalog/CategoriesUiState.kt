package com.example.catalog

import com.example.foodie_api.model.Category

interface CategoriesUiState {
    data class Success(val categories: List<Category>) : CategoriesUiState
    data object Empty : CategoriesUiState
    data object Error : CategoriesUiState
    data object Loading : CategoriesUiState
}