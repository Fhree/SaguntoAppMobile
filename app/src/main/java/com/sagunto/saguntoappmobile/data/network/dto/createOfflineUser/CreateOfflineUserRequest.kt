package com.sagunto.saguntoappmobile.data.network.dto.createOfflineUser

import kotlinx.serialization.Serializable

@Serializable
data class CreateOfflineUserRequest (
    val name: String,
    val surname: String,
    val roleId: Int
)