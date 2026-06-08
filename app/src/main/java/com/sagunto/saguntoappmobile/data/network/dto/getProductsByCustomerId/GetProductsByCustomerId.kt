package com.sagunto.saguntoappmobile.data.network.dto.getProductsByCustomerId

import kotlinx.serialization.Serializable

@Serializable
data class GetProductsByCustomerId (
    val id: Int,
    val name: String,
    val price: Double
)