package com.sagunto.saguntoappmobile.ui.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sagunto.saguntoappmobile.domain.models.Product
import com.sagunto.saguntoappmobile.domain.interfaces.IProductRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AddProductViewModel(
    private val repository: IProductRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    var name = mutableStateOf("")
    var publicPrice = mutableStateOf("")
    var privatePrice = mutableStateOf("")

    val isNameValid: Boolean
        get() = name.value.isNotBlank() && name.value.isNotEmpty()
    val isPublicPriceValid: Boolean
        get() = publicPrice.value.toDoubleOrNull()?.let { it > 0.0 } ?: false
    val isPrivatePriceValid: Boolean
        get() = privatePrice.value.toDoubleOrNull()?.let { it > 0.0 } ?: false
    val isFormValid: Boolean
        get() = isNameValid && isPublicPriceValid && isPrivatePriceValid

    var isNameTouched = mutableStateOf(false)
    var isPublicPriceTouched = mutableStateOf(false)
    var isPrivatePriceTouched = mutableStateOf(false)

    fun saveProduct() {
        if (!isFormValid) return

        viewModelScope.launch {
            _isLoading.value = true

            val product = Product(
                name = name.value,
                publicPrice = publicPrice.value.toDoubleOrNull() ?: 0.0,
                privatePrice = privatePrice.value.toDoubleOrNull() ?: 0.0
            )

            val result = repository.addProduct(product)

            result.fold(
                onSuccess = {
                    _uiEvent.emit(UiEvent.ShowToast("Producto añadido correctamente"))
                },
                onFailure = {
                    _uiEvent.emit(UiEvent.ShowToast("Error al añadir el producto"))
                }
            )
            if (result.isSuccess) {
                name.value = ""
                isNameTouched = mutableStateOf(false)
                publicPrice.value = ""
                isPublicPriceTouched = mutableStateOf(false)
                privatePrice.value = ""
                isPrivatePriceTouched = mutableStateOf(false)
            }
            _isLoading.value = false
        }
    }

    sealed class UiEvent {
        data class ShowToast(val message: String) : UiEvent()
    }
}