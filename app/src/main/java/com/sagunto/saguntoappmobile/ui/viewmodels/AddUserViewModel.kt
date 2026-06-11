package com.sagunto.saguntoappmobile.ui.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sagunto.saguntoappmobile.data.network.dto.createUser.CreateUserRequest
import com.sagunto.saguntoappmobile.data.interfaces.IUserRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AddUserViewModel(
    private val repository: IUserRepository
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
        get() = name.value.isNotBlank() && name.value.isNotEmpty()
    val isSurnameValid: Boolean
        get() = surname.value.isNotBlank() && surname.value.isNotEmpty()
    val isFormValid: Boolean
        get() = isNameValid && isSurnameValid


    fun saveUser() {
        if(!isFormValid) return

        viewModelScope.launch {
            _isLoading.value = true

            val user = CreateUserRequest(
                name = name.value,
                surname = surname.value,
                roleId = 2 //Role Id basic user
            )

            val result = repository.addUser(user)

            result.fold(
                onSuccess = { responseData ->
                    _generatedSaguntinoCode.value = responseData.saguntinoCode
                    _showCodeDialog.value = true
                },
                onFailure = {
                    _uiEvent.emit(UiEvent.ShowToast("Error al añadir el usuario"))
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

    fun dismissDialog() {
        _showCodeDialog.value = false
        _generatedSaguntinoCode.value = ""
    }
}