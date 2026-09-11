package com.sagunto.saguntoappmobile.data.network.dto.getProducts

import kotlinx.serialization.Serializable

@Serializable
data class Product (
    val id: Int,
    val name: String,
    val priceMember: Double,
    val priceGuest: Double
)