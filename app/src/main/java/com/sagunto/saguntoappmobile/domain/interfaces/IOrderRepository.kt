package com.sagunto.saguntoappmobile.domain.interfaces

import com.sagunto.saguntoappmobile.data.network.dto.createOrder.CreateOrderRequest

interface IOrderRepository {
    suspend fun addOrder(order: CreateOrderRequest): Result<String>
}