package com.sagunto.saguntoappmobile.data.network.dto.createUser

import kotlinx.serialization.Serializable

@Serializable
class CreateUserRequest (
    val name: String,
    val surname: String,
    val roleId: Int
)