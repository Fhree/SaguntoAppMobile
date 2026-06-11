package com.sagunto.saguntoappmobile.data.network.dto.getProducts

import kotlinx.serialization.Serializable

@Serializable
data class GetProductsResponse (
    val products: List<Product>
)

@Serializable
data class Product (
    val id: Int,
    val name: String,
    val price: Double,
)