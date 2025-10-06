package com.bowleu.foodentify.ui.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bowleu.foodentify.domain.model.Product
import com.bowleu.foodentify.domain.usecase.GetProductUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val getProductUseCase: GetProductUseCase
) : ViewModel() {

    private val _productId = MutableStateFlow<Long>(0)
    private val _uiLoadingState = MutableStateFlow(UiLoadingState.IDLE)
    val uiLoadingState: StateFlow<UiLoadingState> = _uiLoadingState.asStateFlow()

    val productState: StateFlow<Product?> = _productId
        .flatMapLatest { id ->
            flow {
                _uiLoadingState.value = UiLoadingState.LOADING
                try {
                    val product = getProductUseCase(id).firstOrNull() // пример получения
                    if (product != null) {
                        _uiLoadingState.value = UiLoadingState.SUCCESS
                        emit(product)
                    } else {
                        _uiLoadingState.value = UiLoadingState.ERROR
                        emit(null)
                    }
                } catch (_: Exception) {
                    _uiLoadingState.value = UiLoadingState.ERROR
                    emit(null)
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = null
        )

    fun setProductId(id: Long) {
        _productId.value = id
    }
}

enum class UiLoadingState {
    IDLE,
    LOADING,
    ERROR,
    SUCCESS
}