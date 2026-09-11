package com.sagunto.saguntoappmobile.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val priceMember: Double,
    val priceGuest: Double
)