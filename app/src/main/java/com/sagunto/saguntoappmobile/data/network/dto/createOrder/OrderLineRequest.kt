package com.sagunto.saguntoappmobile.data.network.dto.createOrder

import android.view.inputmethod.TextSnapshot
import kotlinx.serialization.Serializable

@Serializable
data class OrderLineRequest (
    val productId: Int,
    val name: String,
    val quantity: Int,
    val priceSnapshot: Double
)