package com.sagunto.saguntoappmobile.data.repository

import android.util.Log
import com.sagunto.saguntoappmobile.BuildConfig
import com.sagunto.saguntoappmobile.domain.models.Product
import com.sagunto.saguntoappmobile.domain.interfaces.IProductRepository
import com.sagunto.saguntoappmobile.data.network.dto.CreateProductRequest
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess

class ProductRepository(
    private val httpClient: HttpClient
) : IProductRepository {

    override suspend fun addProduct(product: Product): Result<Unit> {
        return try {
            val request = CreateProductRequest(
                name = product.name,
                priceMember = product.publicPrice,
                priceGuest = product.privatePrice
            )

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
}