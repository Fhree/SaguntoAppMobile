package com.sagunto.saguntoappmobile.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PaymentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun enqueuePayment(payment: PaymentEntity): Long

    @Query("SELECT * FROM payments_queue WHERE syncStatus = 'PENDING' ORDER BY createdAt ASC")
    suspend fun getPendingPayments(): List<PaymentEntity>

    @Query("UPDATE payments_queue SET syncStatus = 'SYNCED' WHERE localId = :localId")
    suspend fun markPaymentSynced(localId: Long): Int

    @Query("DELETE FROM payments_queue WHERE syncStatus = 'SYNCED'")
    suspend fun purgeSyncedPayments(): Int
}