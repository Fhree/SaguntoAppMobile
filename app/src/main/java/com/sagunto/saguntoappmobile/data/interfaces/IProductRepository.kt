package com.sagunto.saguntoappmobile.data.interfaces

import com.sagunto.saguntoappmobile.data.local.ProductEntity
import com.sagunto.saguntoappmobile.data.network.dto.createProduct.CreateProductRequest
import kotlinx.coroutines.flow.Flow

interface IProductRepository {
    suspend fun addProduct(request: CreateProductRequest): Result<Unit>
    suspend fun syncProducts(): Result<Unit>
    fun getAllProductsLocal(): Flow<List<ProductEntity>>
}