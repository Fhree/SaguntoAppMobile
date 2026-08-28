package com.sagunto.saguntoappmobile.data.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.gson.Gson
import com.sagunto.saguntoappmobile.data.local.OrderDao
import com.sagunto.saguntoappmobile.data.network.dto.createOrder.BulkCreateOrdersRequest
import com.sagunto.saguntoappmobile.data.network.dto.createOrder.CreateNewOrderRequest
import com.sagunto.saguntoappmobile.data.network.dto.createOrder.OrderLineDto
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SyncOrdersWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams), KoinComponent {

    private val orderDao: OrderDao by inject()
    private val httpClient: HttpClient by inject()

    override suspend fun doWork(): Result {
        return try {
            val pendingOrders = orderDao.getPendingOrders()

            if (pendingOrders.isEmpty()) {
                return Result.success()
            }

            val gson = Gson()
            val bulkRequest = BulkCreateOrdersRequest(
                orders = pendingOrders.map { entity ->
                    val deserializedLines = gson.fromJson(entity.linesPayload, Array<OrderLineDto>::class.java).toList()

                    CreateNewOrderRequest(
                        id = entity.id,
                        isPaid = entity.isPaid,
                        userId = entity.userId,
                        customerId = entity.customerId,
                        products = deserializedLines
                    )
                }
            )


            val response = httpClient.post("api/orders/bulk") {
                contentType(ContentType.Application.Json)
                setBody(bulkRequest)
            }

            if (response.status.isSuccess()) {

                orderDao.markOrdersAsSynced(pendingOrders.map { it.id })
                Log.d("WORKER", "Sincronizados ${pendingOrders.size} pedidos con éxito.")
                Result.success()
            } else {
                Log.e("WORKER", "Error del backend al sincronizar. Código HTTP: ${response.status.value}")
                Result.retry()
            }
        } catch (e: Exception) {
            Log.e("WORKER", "Fallo de red durante la sincronización masiva", e)
            Result.retry()
        }
    }
}