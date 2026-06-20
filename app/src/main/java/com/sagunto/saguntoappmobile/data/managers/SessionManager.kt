package com.sagunto.saguntoappmobile.data.managers

import com.sagunto.saguntoappmobile.data.network.dto.userProfile.UserProfileResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SessionManager {
    private val _currentUser = MutableStateFlow<UserProfileResponse?>(null)
    val currentUser = _currentUser.asStateFlow()

    fun saveUserSession(user: UserProfileResponse) {
        _currentUser.value = user
    }

    fun clearSession() {
        _currentUser.value = null
    }
}