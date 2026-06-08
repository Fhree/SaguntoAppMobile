package com.sagunto.saguntoappmobile.data.network.dto.createProduct

import kotlinx.serialization.Serializable

@Serializable
data class CreateProductRequest(
    val name: String,
    val priceMember: Double,
    val priceGuest: Double
)