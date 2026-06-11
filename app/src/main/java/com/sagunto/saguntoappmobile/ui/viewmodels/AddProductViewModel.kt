package com.sagunto.saguntoappmobile.ui.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sagunto.saguntoappmobile.data.network.dto.createProduct.CreateProductRequest
import com.sagunto.saguntoappmobile.data.interfaces.IProductRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class AddProductViewModel(
    private val repository: IProductRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()


    var name = mutableStateOf("")
    var priceGuest = mutableStateOf("")
    var priceMember = mutableStateOf("")

    val isNameValid: Boolean
        get() = name.value.isNotBlank() && name.value.isNotEmpty()
    val isPriceGuestValid: Boolean
        get() = priceGuest.value.toDoubleOrNull()?.let { it > 0.0 } ?: false
    val isPriceMemberValid: Boolean
        get() = priceMember.value.toDoubleOrNull()?.let { it > 0.0 } ?: false
    val isFormValid: Boolean
        get() = isNameValid && isPriceGuestValid && isPriceMemberValid

    var isNameTouched = mutableStateOf(false)
    var isPriceGuestTouched = mutableStateOf(false)
    var isPriceMemberTouched = mutableStateOf(false)

    fun saveProduct() {
        if (!isFormValid) return

        viewModelScope.launch {
            _isLoading.value = true

            val product = CreateProductRequest(
                name = name.value,
                priceGuest = priceGuest.value.toDoubleOrNull() ?: 0.0,
                priceMember = priceMember.value.toDoubleOrNull() ?: 0.0
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
                priceGuest.value = ""
                isPriceGuestTouched = mutableStateOf(false)
                priceMember.value = ""
                isPriceMemberTouched = mutableStateOf(false)
            }
            _isLoading.value = false
        }
    }

    sealed class UiEvent {
        data class ShowToast(val message: String) : UiEvent()
    }
}