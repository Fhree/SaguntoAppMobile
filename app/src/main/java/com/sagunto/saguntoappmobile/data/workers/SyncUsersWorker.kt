package com.sagunto.saguntoappmobile.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.sagunto.saguntoappmobile.data.interfaces.IUserRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SyncUsersWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams), KoinComponent {

    private val userRepository: IUserRepository by inject()

    override suspend fun doWork(): Result {
        Log.i("SYNC_USERS_WORKER", "Iniciando sincronización de saguntinos...")

        return try {
            val result = userRepository.syncSaguntinos()

            if (result.isSuccess) {
                Log.i("SYNC_USERS_WORKER", "Saguntinos sincronizados con éxito.")
                Result.success()
            } else {
                Log.w("SYNC_USERS_WORKER", "Fallo en la API de usuarios, reintentando.")
                Result.retry()
            }
        } catch (e: Exception) {
            Log.e("SYNC_USERS_WORKER", "Error crítico al sincronizar saguntinos", e)
            Result.retry()
        }
    }
}