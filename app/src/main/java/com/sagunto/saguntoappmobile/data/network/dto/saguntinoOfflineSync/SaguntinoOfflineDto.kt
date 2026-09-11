package com.sagunto.saguntoappmobile.data.network.dto.saguntinoOfflineSync

import kotlinx.serialization.Serializable

@Serializable
data class SaguntinoOfflineDto(
    val id: Int,
    val name: String,
    val surname: String,
    val saguntinoCode: String,
    val normalizedSearch: String
)