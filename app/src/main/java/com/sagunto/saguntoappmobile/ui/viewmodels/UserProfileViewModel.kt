package com.sagunto.saguntoappmobile.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.sagunto.saguntoappmobile.data.managers.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class UserProfileViewModel(
    sessionManager: SessionManager
) : ViewModel() {

    private val _saguntinoCode = MutableStateFlow("")
    val saguntinoCode: StateFlow<String> = _saguntinoCode.asStateFlow()

    init{
        _saguntinoCode.value = sessionManager.currentUser.value?.saguntinoCode ?: ""
    }
}
