package com.sagunto.saguntoappmobile.data.network.dto.createUser

import kotlinx.serialization.Serializable

@Serializable
data class CreateUserResponse (
    val id: Int,
    val name: String,
    val surname: String,
    val saguntinoCode: String,
    val roleId: Int
)