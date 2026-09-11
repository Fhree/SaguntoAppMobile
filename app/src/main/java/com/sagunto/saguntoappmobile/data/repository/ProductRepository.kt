package com.sagunto.saguntoappmobile.data.repository

import android.util.Log
import com.sagunto.saguntoappmobile.data.interfaces.IProductRepository
import com.sagunto.saguntoappmobile.data.local.ProductDao
import com.sagunto.saguntoappmobile.data.local.ProductEntity
import com.sagunto.saguntoappmobile.data.network.dto.createProduct.CreateProductRequest
import com.sagunto.saguntoappmobile.data.network.dto.getProducts.Product
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import kotlinx.coroutines.flow.Flow

class ProductRepository(
    private val httpClient: HttpClient,
    private val productDao: ProductDao
) : IProductRepository {

    override suspend fun addProduct(request: CreateProductRequest): Result<Unit> {
        return try {
            val response = httpClient.post("api/products") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }


            if (response.status.isSuccess()) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Fallo en la API. Código HTTP: ${response.status.value}"))
            }

        } catch (e: Exception) {
            Log.e("API_ERROR", "💥 Ha fallado la petición HTTP", e)
            return Result.failure(e)
        }
    }

    override suspend fun syncProducts(): Result<Unit> {
        return try {
            val response = httpClient.get("api/products") {
                contentType(ContentType.Application.Json)
            }

            if (response.status.isSuccess()) {
                // 🛠️ Cambiamos GetProductsResponse por List<Product>
                val networkData = response.body<List<Product>>()

                val entities = networkData.map { dto ->
                    ProductEntity(
                        id = dto.id,
                        name = dto.name,
                        priceMember = dto.priceMember,
                        priceGuest = dto.priceGuest
                    )
                }

                productDao.insertAll(entities)
                Result.success(Unit)
            } else {
                Result.failure(Exception("Fallo HTTP: ${response.status.value}"))
            }
        } catch (e: Exception) {
            Log.e("API_ERROR", "💥 Fallo de red en syncProducts", e)
            Result.failure(e)
        }
    }

    override fun getAllProductsLocal(): Flow<List<ProductEntity>> {
        // 🛠️ El ViewModel se suscribe directamente a SQLite
        return productDao.getAllProductsFlow()
    }
}