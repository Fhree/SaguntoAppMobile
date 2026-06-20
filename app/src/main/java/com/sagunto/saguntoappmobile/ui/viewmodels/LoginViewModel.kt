package com.sagunto.saguntoappmobile.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.sagunto.saguntoappmobile.data.interfaces.IAuthRepository
import com.sagunto.saguntoappmobile.data.interfaces.IUserRepository
import com.sagunto.saguntoappmobile.data.managers.SessionManager
import com.sagunto.saguntoappmobile.ui.viewmodels.AddProductViewModel.UiEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: IAuthRepository,
    private val userRepository: IUserRepository,
    private val sessionManager: SessionManager
) : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    private val _loginStatus = MutableStateFlow<String?>(null)
    val loginStatus: StateFlow<String?> = _loginStatus.asStateFlow()


    fun loginUser() {
        val currentEmail = _username.value.trim()
        val currentPassword = _password.value.trim()

        if (currentEmail.isEmpty() || currentPassword.isEmpty()) {
            _loginStatus.value = "El email y la contraseña no pueden estar vacíos."
            return
        }

        _loginStatus.value = "Autenticando..."

        viewModelScope.launch {

            val isFirebaseSuccess = authRepository.loginWithEmail(currentEmail, currentPassword)

            if (!isFirebaseSuccess) {
                _loginStatus.value = "El email y contraseña no coinciden."
                return@launch
            }
            val firebaseUid = FirebaseAuth.getInstance().currentUser?.uid

            if (firebaseUid == null) {
                _loginStatus.value = "Error crítico: No se pudo obtener la identidad del usuario."
                return@launch
            }

            _loginStatus.value = "Recuperando perfil de usuario..."

            val result = userRepository.getUserProfile(firebaseUid)
            result.fold(
                onSuccess = { userProfileData ->
                    sessionManager.saveUserSession(userProfileData)
                    _loginStatus.value = "¡Login exitoso!"
                },
                onFailure = { exception ->
                    _loginStatus.value = "Error del servidor: ${exception.message}"
                }
            )
        }
    }

    fun updateUsername(newUsername: String) {
        _username.value = newUsername
    }

    fun updatePassword(newPassword: String) {
        _password.value = newPassword
    }
}