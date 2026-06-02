package com.sagunto.saguntoappmobile.data.network.dto

import kotlinx.serialization.Serializable

@Serializable
class CreateProductRequest(
    val name: String,
    val priceMember: Double,
    val priceGuest: Double
)