package com.sagunto.saguntoappmobile.data.network.dto.unpaidOrder

import kotlinx.serialization.Serializable

@Serializable
data class UnpaidOrderLines(
    val name: String,
    val quantity: Int,
    val price: Double
)

@Serializable
data class PayOrdersRequest(
    val orderIds: List<String>
)

@Serializable
data class UnpaidOrderResponse(
    val id: String, // 🛠️ Cambiado de Int a String (UUID)
    val total: Double,
    val date: String,
    val orderLines: List<UnpaidOrderLines>? = null
)