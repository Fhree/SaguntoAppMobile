package com.sagunto.saguntoappmobile.ui.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sagunto.saguntoappmobile.domain.interfaces.IUserRepository
import com.sagunto.saguntoappmobile.domain.models.User
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AddUserViewModel(
    private val repository: IUserRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()
    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    var name = mutableStateOf("")
    var surname =  mutableStateOf("")

    var isNameTouched = mutableStateOf(false)
    var isSurnameTouched = mutableStateOf(false)

    val isNameValid: Boolean
        get() = name.value.isNotBlank() && name.value.isNotEmpty()
    val isSurnameValid: Boolean
        get() = surname.value.isNotBlank() && surname.value.isNotEmpty()
    val isFormValid: Boolean
        get() = isNameValid && isSurnameValid


    fun saveUser() {
        if(!isFormValid) return

        viewModelScope.launch {
            _isLoading.value = true

            val user = User(
                name = name.value,
                surname = surname.value
            )

            val result = repository.addUser(user)

            result.fold(
                onSuccess = {
                    _uiEvent.emit(UiEvent.ShowToast("Saguntino añadido correctamente"))
                },
                onFailure = {
                    _uiEvent.emit(UiEvent.ShowToast("Error al añadir el producto"))
                }
            )
            if (result.isSuccess) {
                name.value = ""
                isNameTouched = mutableStateOf(false)
                surname.value = ""
                isSurnameTouched = mutableStateOf(false)
            }

            _isLoading.value = false
        }
    }

    sealed class UiEvent {
        data class ShowToast(val message: String) : UiEvent()
    }
}