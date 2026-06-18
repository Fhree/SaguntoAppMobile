package com.sagunto.saguntoappmobile.ui.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sagunto.saguntoappmobile.data.network.dto.createUser.CreateUserRequest
import com.sagunto.saguntoappmobile.data.interfaces.IUserRepository
import com.sagunto.saguntoappmobile.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AddUserViewModel(
    private val userRepository: IUserRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _showCodeDialog = MutableStateFlow(false)
    val showCodeDialog: StateFlow<Boolean> = _showCodeDialog.asStateFlow()

    private val _generatedSaguntinoCode = MutableStateFlow("")
    val generatedSaguntinoCode: StateFlow<String> = _generatedSaguntinoCode.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()
    
    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    var name = mutableStateOf("")
    var surname =  mutableStateOf("")

    var isNameTouched = mutableStateOf(false)
    var isSurnameTouched = mutableStateOf(false)

    val isNameValid: Boolean
        get() = name.value.isNotBlank()
    val isSurnameValid: Boolean
        get() = surname.value.isNotBlank()
    val isFormValid: Boolean
        get() = isNameValid && isSurnameValid

    fun saveUser() {
        if(!isFormValid) return

        viewModelScope.launch {
            _isLoading.value = true

            val token = authRepository.getJwtToken()
            if (token == null) {
                _uiEvent.emit(UiEvent.ShowToast("Error de sesión: No se pudo obtener el token"))
                _isLoading.value = false
                return@launch
            }

            val user = CreateUserRequest(
                name = name.value,
                surname = surname.value
            )

            val result = userRepository.createUser(user, token)

            result.fold(
                onSuccess = { responseData ->
                    _generatedSaguntinoCode.value = responseData.saguntinoCode
                    _showCodeDialog.value = true

                    name.value = ""
                    surname.value = ""
                    isNameTouched.value = false
                    isSurnameTouched.value = false
                },
                onFailure = {
                    _uiEvent.emit(UiEvent.ShowToast("Error al añadir el usuario"))
                }
            )

            _isLoading.value = false
        }
    }

    sealed class UiEvent {
        data class ShowToast(val message: String) : UiEvent()
    }

    fun dismissDialog() {
        _showCodeDialog.value = false
        _generatedSaguntinoCode.value = ""
    }
}
