package com.sagunto.saguntoappmobile.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sagunto.saguntoappmobile.data.interfaces.IAuthRepository
import com.sagunto.saguntoappmobile.data.interfaces.IUserRepository
import com.sagunto.saguntoappmobile.data.network.dto.userRegister.UserRegisterRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class UserRegisterViewModel(
    private val authRepository: IAuthRepository,
    private val userRepository: IUserRepository
) : ViewModel() {

    val name = MutableStateFlow("")
    val surname = MutableStateFlow("")
    val email = MutableStateFlow("")
    val password = MutableStateFlow("")

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    private val _successCode = MutableStateFlow<String?>(null)
    val successCode = _successCode.asStateFlow()

    fun register() {
        if (name.value.isBlank() || email.value.isBlank() || password.value.isBlank()) {
            _errorMessage.value = "Por favor, rellena los campos obligatorios."
            return
        }

        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            val authResult = authRepository.registerUser(email.value, password.value)

            if (authResult.isSuccess) {
                try {
                    val tokenResult = authRepository.currentUser.value?.getIdToken(true)?.await()
                    val token = tokenResult?.token

                    if (token != null) {
                        val request = UserRegisterRequest(
                            name = name.value,
                            surname = surname.value
                        )

                        val netResult = userRepository.userRegister(request)

                        if (netResult.isSuccess) {
                            val code = netResult.getOrNull()?.saguntinoCode ?: "ERROR_CODE"
                            _successCode.value = code
                            userRepository.fetchUserProfile()
                        } else {
                            _errorMessage.value = "Fallo al registrar en el servidor de Sagunto."
                        }
                    } else {
                        _errorMessage.value = "No se pudo obtener la credencial de seguridad."
                    }
                } catch (e: Exception) {
                    _errorMessage.value = e.message ?: "Error desconocido"
                }
            } else {
                _errorMessage.value = "Fallo al crear la cuenta. Revisa el correo/contraseña."
            }

            _isLoading.value = false // Siempre apagamos la carga al terminar
        }
    }

    fun resetSuccessState() {
        _successCode.value = null
    }
}