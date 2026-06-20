package com.sagunto.saguntoappmobile.data.network.dto.userProfile

import kotlinx.serialization.Serializable

@Serializable
data class UserProfileResponse(
    val id: Int,
    val name: String,
    val saguntinoCode: String,
    val roleId: Int
)
