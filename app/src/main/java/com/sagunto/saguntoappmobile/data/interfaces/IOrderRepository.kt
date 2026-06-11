package com.sagunto.saguntoappmobile.data.interfaces

import com.sagunto.saguntoappmobile.data.network.dto.createOrder.CreateOrderRequest
import com.sagunto.saguntoappmobile.data.network.dto.unpaidOrder.UnpaidOrderResponse

interface IOrderRepository {
    suspend fun addOrder(order: CreateOrderRequest): Result<String>
    suspend fun getUnpaidOrders(customerId: Int): Result<List<UnpaidOrderResponse>>
    suspend fun payOrders(customerId: Int): Result<String>
}