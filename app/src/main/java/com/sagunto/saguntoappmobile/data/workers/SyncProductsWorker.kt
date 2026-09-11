package com.sagunto.saguntoappmobile.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.sagunto.saguntoappmobile.data.interfaces.IProductRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SyncProductsWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams), KoinComponent {

    // 🛠️ Inyectamos tu repositorio directamente desde Koin
    private val productRepository: IProductRepository by inject()

    override suspend fun doWork(): Result {
        Log.i("SYNC_WORKER", "Iniciando sincronización de productos en segundo plano...")

        return try {
            val result = productRepository.syncProducts()

            if (result.isSuccess) {
                Log.i("SYNC_WORKER", "Sincronización completada con éxito.")
                Result.success()
            } else {
                Log.w("SYNC_WORKER", "Fallo en la API, reintentando más tarde.")
                Result.retry()
            }
        } catch (e: Exception) {
            Log.e("SYNC_WORKER", "Error crítico al sincronizar", e)
            Result.retry()
        }
    }
}