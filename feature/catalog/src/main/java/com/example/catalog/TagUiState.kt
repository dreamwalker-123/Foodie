package com.example.catalog

import com.example.model.Tag

interface TagUiState {
    data class Success(val tags: List<Tag>) : TagUiState
    data object Empty : TagUiState
    data object Error : TagUiState
    data object Loading : TagUiState
}