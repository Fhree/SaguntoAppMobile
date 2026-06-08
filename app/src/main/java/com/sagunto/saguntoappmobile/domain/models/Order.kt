package com.sagunto.saguntoappmobile.domain.models

import kotlinx.datetime.Instant

data class Order (
    val id: Int,
    val createdAt: Instant,
    val total: Double,
    val isPaid: Boolean,
    val userId: Int,
    val customerId: Int?
    )