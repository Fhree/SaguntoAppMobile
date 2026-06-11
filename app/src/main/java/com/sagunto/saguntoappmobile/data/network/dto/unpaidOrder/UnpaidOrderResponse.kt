package com.sagunto.saguntoappmobile.data.network.dto.unpaidOrder

import kotlinx.serialization.Serializable

@Serializable
data class UnpaidOrderResponse (
    val id: Int,
    val total: Double,
    val date: String,
    val orderLines: List<UnpaidOrderLines>?
)

@Serializable
data class UnpaidOrderLines(
    val name: String,
    val quantity: Int,
    val price: Double
)