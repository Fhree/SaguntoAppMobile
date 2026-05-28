package com.sagunto.saguntoappmobile.domain.interfaces

import com.sagunto.saguntoappmobile.domain.models.Product

interface IProductRepository {
    suspend fun addProduct(product: Product): Result<Unit>
}