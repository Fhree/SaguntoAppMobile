package com.sagunto.saguntoappmobile.data.network.dto.userRegister

import kotlinx.serialization.Serializable

@Serializable
data class UserRegisterResponse (
    val id: Int,
    val name: String,
    val surname: String,
    val saguntinoCode: String,
    val roleId: Int
)