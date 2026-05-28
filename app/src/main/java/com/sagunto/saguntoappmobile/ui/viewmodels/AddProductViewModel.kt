package com.sagunto.saguntoappmobile.ui.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sagunto.saguntoappmobile.domain.models.Product
import com.sagunto.saguntoappmobile.domain.interfaces.IProductRepository
import kotlinx.coroutines.launch

class AddProductViewModel(
    private val repository: IProductRepository
) : ViewModel() {

    var name = mutableStateOf("")
    var publicPrice = mutableStateOf("")
    var privatePrice = mutableStateOf("")

    fun saveProduct() {
        viewModelScope.launch {
            val product = Product(
                name = name.value,
                publicPrice = publicPrice.value.toDoubleOrNull() ?: 0.0,
                privatePrice = privatePrice.value.toDoubleOrNull() ?: 0.0
            )

            val result = repository.addProduct(product)
            if (result.isSuccess) {
                name.value = ""
                publicPrice.value = ""
                privatePrice.value = ""
            }
        }
    }
}