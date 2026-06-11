package com.sagunto.saguntoappmobile.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.sagunto.saguntoappmobile.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class LoginViewModel() : ViewModel() {
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

        auth.signInWithEmailAndPassword(currentEmail, currentPassword)
        .addOnCompleteListener { task ->
            if (!task.isSuccessful)
                _loginStatus.value = "El email y contraseña no coinciden, vuelve a intentarlo."
        }
    }

    fun updateUsername(newUsername: String) {
        _username.value = newUsername
    }

    fun updatePassword(newPassword: String) {
        _password.value = newPassword
    }
}