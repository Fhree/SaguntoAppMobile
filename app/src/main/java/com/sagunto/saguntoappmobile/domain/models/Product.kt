package com.sagunto.saguntoappmobile.domain.models

data class Product(
    val id: Int = -1,
    val name: String,
    val publicPrice: Double,
    val privatePrice: Double
)