package com.sagunto.saguntoappmobile.domain.interfaces

import com.sagunto.saguntoappmobile.data.network.dto.createProduct.CreateProductRequest
import com.sagunto.saguntoappmobile.data.network.dto.getProductsByCustomerId.GetProductsByCustomerId
import com.sagunto.saguntoappmobile.domain.models.Product

interface IProductRepository {
    suspend fun addProduct(request: CreateProductRequest): Result<Unit>
    suspend fun getProducts(): Result<List<Product>>
    suspend fun getProductsByCustomerId(request: Int): Result<List<GetProductsByCustomerId>>
}