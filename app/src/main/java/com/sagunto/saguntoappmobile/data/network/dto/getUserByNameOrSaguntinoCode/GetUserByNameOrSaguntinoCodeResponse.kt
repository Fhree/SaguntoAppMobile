package com.sagunto.saguntoappmobile.data.network.dto.getUserByNameOrSaguntinoCode

import kotlinx.serialization.Serializable

@Serializable
data class GetUserByNameOrSaguntinoCodeResponse(
    val id: Int = 0,
    val name: String = "",
    val surname: String = "",
    val saguntinoCode: String = ""
)