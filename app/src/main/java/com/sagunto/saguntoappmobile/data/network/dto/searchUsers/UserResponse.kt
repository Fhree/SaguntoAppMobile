package com.sagunto.saguntoappmobile.data.network.dto.searchUsers

import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    val id: Int = 0,
    val name: String = "",
    val surname: String = "",
    val saguntinoCode: String = ""
)