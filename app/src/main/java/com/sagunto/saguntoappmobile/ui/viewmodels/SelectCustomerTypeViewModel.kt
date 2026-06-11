package com.sagunto.saguntoappmobile.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sagunto.saguntoappmobile.data.network.dto.searchUsers.SearchUsersResponse
import com.sagunto.saguntoappmobile.data.network.dto.searchUsers.UserResponse
import com.sagunto.saguntoappmobile.domain.interfaces.IUserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SelectCustomerTypeViewModel(
    private val repository: IUserRepository
): ViewModel() {

    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog.asStateFlow()
    
    private val _messageDialog = MutableStateFlow("")
    val messageDialog: StateFlow<String> = _messageDialog.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _searchResults = MutableStateFlow<List<UserResponse>>(emptyList())
    val searchResults: StateFlow<List<UserResponse>> = _searchResults.asStateFlow()

    private val _isLoading = MutableStateFlow(false)

    val isSaguntinoCodeValid: Boolean = true
    val isSaguntinoCodeTouched: Boolean = false

    fun searchUsers(onNavigateToOrder: (Int) -> Unit) {
        val currentQuery = _searchQuery.value.trim()
        if (currentQuery.isEmpty()) return

        viewModelScope.launch {
            _isLoading.value = true
            
            when (val result = repository.searchUsers(currentQuery)) {
                is SearchUsersResponse.SingleResult -> {
                    onNavigateToOrder(result.user.id)
                }
                is SearchUsersResponse.MultipleResults -> {
                    _messageDialog.value = "Seleccione al saguntino"
                    _searchResults.value = result.users
                    _showDialog.value = true
                }
                is SearchUsersResponse.Error -> {
                    _messageDialog.value = result.message
                    _searchResults.value = emptyList()
                    _showDialog.value = true
                }
            }
            
            _isLoading.value = false
        }
    }

    fun dismissDialog() {
        _showDialog.value = false
        _messageDialog.value = ""
        _searchResults.value = emptyList()
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }
}
