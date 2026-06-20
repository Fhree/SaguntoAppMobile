package com.sagunto.saguntoappmobile.ui.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class UserProfileViewModel : ViewModel() {

    private val _saguntinoCode = MutableStateFlow("")
    val saguntinoCode: StateFlow<String> = _saguntinoCode.asStateFlow()
}
