package com.sagunto.saguntoappmobile.data.repository

import android.util.Log
import com.sagunto.saguntoappmobile.data.interfaces.IProductRepository
import com.sagunto.saguntoappmobile.data.network.dto.createProduct.CreateProductRequest
import com.sagunto.saguntoappmobile.data.network.dto.getProducts.GetProductsResponse
import com.sagunto.saguntoappmobile.data.network.dto.getProductsByCustomerId.GetProductsByCustomerId
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess

class ProductRepository(
    private val httpClient: HttpClient
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

    override suspend fun getProducts(): Result<GetProductsResponse> {
        return try{
            val response = httpClient.get("api/products"){
                contentType(ContentType.Application.Json)
            }

            if (response.status.isSuccess()) {
                Result.success(response.body())
            } else {
                Result.failure(Exception("Fallo en la API. Código HTTP: ${response.status.value}"))
            }
        } catch (e: Exception) {
            Log.e("API_ERROR", "💥 Ha fallado la petición HTTP", e)
            return Result.failure(e)
        }
    }

    override suspend fun getProductsByCustomerId(request: Int): Result<List<GetProductsByCustomerId>> {
        return try{
            val response = httpClient.get("api/products/$request"){
                contentType(ContentType.Application.Json)
            }

            if (response.status.isSuccess()) {
                Result.success(response.body())
            } else {
                Result.failure(Exception("Fallo en la API. Código HTTP: ${response.status.value}"))
            }
        } catch (e: Exception) {
            Log.e("API_ERROR", "💥 Ha fallado la petición HTTP", e)
            return Result.failure(e)
        }
    }
}