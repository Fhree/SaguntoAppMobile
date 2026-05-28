package com.sagunto.saguntoappmobile.data.repository

import com.sagunto.saguntoappmobile.domain.models.Product
import com.sagunto.saguntoappmobile.domain.interfaces.IProductRepository
import com.sagunto.saguntoappmobile.data.network.dto.CreateProductRequest

class ProductRepository : IProductRepository {

    override suspend fun addProduct(product: Product): Result<Unit> {
        try {
            // 1. Mapeamos del Dominio puro al DTO sucio
            val request = CreateProductRequest(
                name = product.name,
                publicPrice = product.publicPrice,
                privatePrice = product.privatePrice
            )

            // 2. Aquí harías tu llamada HttpClient al backend de .NET
            // o guardarías en SQLite con EF Core/Room.
            // api.postProduct(dto)

            return Result.success(Unit)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}