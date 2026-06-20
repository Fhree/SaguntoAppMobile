package com.sagunto.saguntoappmobile.data.network.dto.userRegister

import kotlinx.serialization.Serializable

@Serializable
data class UserRegisterRequest (
    val name: String,
    val surname: String
)