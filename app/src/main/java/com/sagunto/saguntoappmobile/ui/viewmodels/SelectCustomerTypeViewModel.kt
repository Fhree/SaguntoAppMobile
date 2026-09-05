package com.sagunto.saguntoappmobile.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sagunto.saguntoappmobile.data.network.dto.searchUsers.SearchUsersResponse
import com.sagunto.saguntoappmobile.data.network.dto.searchUsers.UserResponse
import com.sagunto.saguntoappmobile.data.interfaces.IUserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SelectCustomerTypeViewModel(): ViewModel() {

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


}
