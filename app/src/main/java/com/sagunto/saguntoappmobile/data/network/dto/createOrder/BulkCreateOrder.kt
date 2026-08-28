package com.sagunto.saguntoappmobile.data.network.dto.createOrder

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class OrderLineDto(
    @SerializedName("productId") val productId: Int,
    @SerializedName("quantity") val quantity: Int,
    @SerializedName("priceSnapshot") val priceSnapshot: Double
)

@Serializable
data class CreateNewOrderRequest(
    @SerializedName("id") val id: String,
    @SerializedName("isPaid") val isPaid: Boolean,
    @SerializedName("userId") val userId: Int,
    @SerializedName("customerId") val customerId: Int?,
    @SerializedName("products") val products: List<OrderLineDto>
)

@Serializable
data class BulkCreateOrdersRequest(
    @SerializedName("orders") val orders: List<CreateNewOrderRequest>
)