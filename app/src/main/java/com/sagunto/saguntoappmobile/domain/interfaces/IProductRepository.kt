package com.sagunto.saguntoappmobile.domain.interfaces

import com.sagunto.saguntoappmobile.data.network.dto.createProduct.CreateProductRequest
import com.sagunto.saguntoappmobile.data.network.dto.getProducts.GetProductsResponse
import com.sagunto.saguntoappmobile.data.network.dto.getProductsByCustomerId.GetProductsByCustomerId

interface IProductRepository {
    suspend fun addProduct(request: CreateProductRequest): Result<Unit>
    suspend fun getProducts(): Result<GetProductsResponse>
    suspend fun getProductsByCustomerId(request: Int): Result<List<GetProductsByCustomerId>>
}