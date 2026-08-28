package com.sagunto.saguntoappmobile.data.network.dto.createOrder

import kotlinx.serialization.Serializable

@Serializable
data class OrderLineRequest (
    val productId: Int,
    val name: String,
    val quantity: Int,
    val priceSnapshot: Double
)