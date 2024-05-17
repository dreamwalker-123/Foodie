package com.example.basket

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.repository.NetworkRepository
import com.example.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class BasketViewModel @Inject constructor(
    private val productsRepository: NetworkRepository
) : ViewModel() {
    val cartFlow = productsRepository.getCart().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyMap()
    )

    fun addProductInCart(product: Product) = productsRepository.addProductInCart(product)

    fun removeProductFromCart(product: Product) = productsRepository.removeProductFromCart(product)
}