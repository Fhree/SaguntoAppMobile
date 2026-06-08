package com.sagunto.saguntoappmobile.domain.models

data class OrderLine (
    val id: Int,
    val productId: Int,
    val quantity: Int,
    val priceSnapshot: Double,
    val orderId: Int
)
