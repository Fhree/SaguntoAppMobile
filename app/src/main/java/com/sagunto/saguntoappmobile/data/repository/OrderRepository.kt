package com.sagunto.saguntoappmobile.data.repository

import android.util.Log
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.google.gson.Gson
import com.sagunto.saguntoappmobile.data.local.OrderDao
import com.sagunto.saguntoappmobile.data.local.OrderEntity
import com.sagunto.saguntoappmobile.data.network.dto.createOrder.CreateOrderRequest
import com.sagunto.saguntoappmobile.data.network.dto.unpaidOrder.UnpaidOrderResponse
import com.sagunto.saguntoappmobile.data.interfaces.IOrderRepository
import com.sagunto.saguntoappmobile.data.workers.SyncOrdersWorker
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.HttpClient
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess

import java.util.UUID

class OrderRepository(
    private val orderDao: OrderDao,
    private val workManager: WorkManager,
    private val httpClient: HttpClient
) : IOrderRepository {

    override suspend fun addOrder(order: CreateOrderRequest): Result<String> {
        return try {
            val orderId = UUID.randomUUID().toString()

            val gson = Gson()
            val linesJson = gson.toJson(order.products)

            val entity = OrderEntity(
                id = orderId,
                isPaid = order.isPaid ?: false,
                userId = order.userId ?: 0,
                customerId = order.customerId,
                linesPayload = linesJson
            )

            orderDao.insertOrder(entity)
            enqueueSyncWorker()

            Result.success("Pedido encolado en modo offline")
        } catch (e: Exception) {
            Log.e("LOCAL_DB_ERROR", "💥 Fallo al guardar en SQLite local", e)
            Result.failure(e)
        }
    }

    private fun enqueueSyncWorker() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val syncWorkRequest = OneTimeWorkRequestBuilder<SyncOrdersWorker>()
            .setConstraints(constraints)
            .build()

        workManager.enqueue(syncWorkRequest)
    }


    override suspend fun getUnpaidOrders(customerId: Int): Result<List<UnpaidOrderResponse>> {
        return try {
            val response = httpClient.get("api/orders/${customerId}/without-payment") {
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
        return try {
            val response = httpClient.post("api/orders/${customerId}/payall") {
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