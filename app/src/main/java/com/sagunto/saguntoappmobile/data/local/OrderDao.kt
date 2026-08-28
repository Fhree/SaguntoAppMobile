package com.sagunto.saguntoappmobile.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import kotlin.jvm.JvmSuppressWildcards

@Dao
@JvmSuppressWildcards
interface OrderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: OrderEntity): Long

    @Query("SELECT * FROM orders_queue WHERE syncStatus = 'PENDING'")
    suspend fun getPendingOrders(): List<OrderEntity>

    @Query("UPDATE orders_queue SET syncStatus = 'SYNCED' WHERE id IN (:orderIds)")
    suspend fun markOrdersAsSynced(orderIds: List<String>): Int

    @Query("SELECT * FROM orders_queue ORDER BY createdAt DESC")
    fun observeAllOrders(): Flow<List<OrderEntity>>

    @Query("DELETE FROM orders_queue WHERE syncStatus = 'SYNCED'")
    suspend fun purgeSyncedOrders(): Int
}