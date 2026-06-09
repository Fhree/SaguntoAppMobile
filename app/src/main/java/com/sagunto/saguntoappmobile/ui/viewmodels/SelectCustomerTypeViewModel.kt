package com.sagunto.saguntoappmobile.ui.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sagunto.saguntoappmobile.data.network.dto.getUserByNameOrSaguntinoCode.GetUserByNameOrSaguntinoCodeResponse
import com.sagunto.saguntoappmobile.domain.interfaces.IUserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private val saguntinoCodeRegex = Regex("^[a-zA-Z]\\d{2}$")

class SelectCustomerTypeViewModel(
    private val repository: IUserRepository
): ViewModel() {

    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog.asStateFlow()
    private val _messageDialog = MutableStateFlow("")
    val messageDialog: StateFlow<String> = _messageDialog.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _searchResults = MutableStateFlow<List<GetUserByNameOrSaguntinoCodeResponse>>(emptyList())
    val searchResults: StateFlow<List<GetUserByNameOrSaguntinoCodeResponse>> = _searchResults.asStateFlow()

    val isSaguntinoCodeValid: Boolean = false
    val isSaguntinoCodeTouched: Boolean = false

    fun searchUsers(onNavigateToOrder: (Int) -> Unit) {
        val currentQuery = _searchQuery.value.trim()
        if (currentQuery.isEmpty()) return

        if (saguntinoCodeRegex.matches(currentQuery)) {
            searchUserBySaguntinoCode(currentQuery, onNavigateToOrder)
        } else {
            searchUsersByName(currentQuery)
        }
    }

    private fun searchUserBySaguntinoCode(code: String, onNavigateToOrder: (Int) -> Unit) {
        viewModelScope.launch {
            repository.getUserBySaguntinoCode(code).fold(
                onSuccess = { user ->
                    onNavigateToOrder(user.id)
                },
                onFailure = {
                    _messageDialog.value = "No existe ningún saguntino con ese código"
                    _searchResults.value = emptyList()
                    _showDialog.value = true
                }
            )
        }
    }

    private fun searchUsersByName(name: String) {
        viewModelScope.launch {
            repository.getUserByName(name).fold(
                onSuccess = { users ->
                    if (users.isEmpty()) {
                        _messageDialog.value = "No existe ningún usuario con ese nombre"
                        _searchResults.value = emptyList()
                    } else {
                        _messageDialog.value = "Seleccione al saguntino"
                        _searchResults.value = users
                    }
                    _showDialog.value = true
                },
                onFailure = {
                    _messageDialog.value = "Error al buscar el nombre en el servidor"
                    _searchResults.value = emptyList()
                    _showDialog.value = true
                }
            )
        }
    }

    fun dismissDialog() {
        _showDialog.value = false
        _messageDialog.value = ""
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }
}