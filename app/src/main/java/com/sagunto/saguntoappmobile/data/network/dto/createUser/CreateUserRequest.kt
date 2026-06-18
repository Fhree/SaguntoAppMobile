package com.sagunto.saguntoappmobile.data.network.dto.createUser

import kotlinx.serialization.Serializable

@Serializable
data class CreateUserRequest (
    val name: String,
    val surname: String,
)