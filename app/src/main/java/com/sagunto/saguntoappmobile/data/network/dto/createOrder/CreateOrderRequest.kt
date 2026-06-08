package com.sagunto.saguntoappmobile.data.network.dto.createOrder

import kotlinx.serialization.Serializable

@Serializable
data class CreateOrderRequest (
    val isPaid: Boolean,
    val userId: Int,
    val customerId: Int,
    val products: List<OrderLineRequest> = emptyList()
    )

