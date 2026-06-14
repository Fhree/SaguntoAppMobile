package com.sagunto.saguntoappmobile.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sagunto.saguntoappmobile.data.interfaces.IUserRepository
import com.sagunto.saguntoappmobile.data.network.dto.createUser.CreateUserRequest
import com.sagunto.saguntoappmobile.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

sealed class RegisterUiState {
    object Idle : RegisterUiState()
    object Loading : RegisterUiState()
    data class Success(val saguntinoCode: String) : RegisterUiState()
    data class Error(val message: String) : RegisterUiState()
}

class UserRegisterViewModel(
    private val authRepository: AuthRepository,
    private val userRepository: IUserRepository
) : ViewModel() {

    val name = MutableStateFlow("")
    val surname = MutableStateFlow("")
    val email = MutableStateFlow("")
    val password = MutableStateFlow("")

    private val _uiState = MutableStateFlow<RegisterUiState>(RegisterUiState.Idle)
    val uiState = _uiState.asStateFlow()

    fun register() {
        if (name.value.isBlank() || email.value.isBlank() || password.value.isBlank()) {
            _uiState.value = RegisterUiState.Error("Por favor, rellena los campos obligatorios.")
            return
        }

        _uiState.value = RegisterUiState.Loading
        viewModelScope.launch {
            val authResult = authRepository.registerUser(email.value, password.value)

            if (authResult.isSuccess) {
                try {
                    val tokenResult = authRepository.currentUser.value?.getIdToken(true)?.await()
                    val token = tokenResult?.token

                    if (token != null) {
                        val request = CreateUserRequest(
                            name = name.value,
                            surname = surname.value,
                            roleId = 2
                        )

                        val netResult = userRepository.addUser(request, token)

                        if (netResult.isSuccess) {
                            val code = netResult.getOrNull()?.saguntinoCode ?: "ERROR_CODE"
                            _uiState.value = RegisterUiState.Success(code)
                            userRepository.fetchUserProfile(token)
                        } else {
                            _uiState.value = RegisterUiState.Error("Fallo al registrar en el servidor de Sagunto.")
                        }
                    } else {
                        _uiState.value = RegisterUiState.Error("No se pudo obtener la credencial de seguridad.")
                    }
                } catch (e: Exception) {
                    _uiState.value = RegisterUiState.Error(e.message ?: "Error desconocido")
                }
            } else {
                _uiState.value = RegisterUiState.Error("Fallo al crear la cuenta. Revisa el correo/contraseña.")
            }
        }
    }
}