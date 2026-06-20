package com.sagunto.saguntoappmobile.data.network.dto.createOfflineUser

import kotlinx.serialization.Serializable

@Serializable
data class CreateOfflineUserResponse (
    val id: Int,
    val name: String,
    val surname: String,
    val saguntinoCode: String,
    val roleId: Int
)