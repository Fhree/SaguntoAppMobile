package com.sagunto.saguntoappmobile.data.repository

import android.util.Log
import com.sagunto.saguntoappmobile.data.network.dto.createOrder.CreateOrderRequest
import com.sagunto.saguntoappmobile.data.network.dto.unpaidOrder.UnpaidOrderResponse
import com.sagunto.saguntoappmobile.domain.interfaces.IOrderRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess

class OrderRepository(
    private val httpClient: HttpClient
) : IOrderRepository {

    override suspend fun addOrder(order: CreateOrderRequest): Result<String> {
        return try{

            val response = httpClient.post("api/orders"){
                contentType(ContentType.Application.Json)
                setBody(order)
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

    override suspend fun getUnpaidOrders(customerId: Int): Result<List<UnpaidOrderResponse>> {
        return try{

            val response = httpClient.get("api/orders/${customerId}/without-payment"){
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

    override suspend fun payOrders(customerId: Int): Result<String> {
        return try{

            val response = httpClient.post("api/orders/${customerId}/payall"){
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